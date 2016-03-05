/*
 * Copyright 2016 Diego Rossi (@_HellPie)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hellpie.mcpe.addongen.content;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import dev.hellpie.mcpe.addongen.R;
import dev.hellpie.mcpe.addongen.ui.fragments.Page;
import dev.hellpie.mcpe.addongen.ui.fragments.WelcomePage;

public class PagesData {

    /**
     * ABSTRACT SETUP DATA CLASS
     */

    private static final String TAG = PagesData.class.getSimpleName();
    protected final Context mContext;
    private final PagesList pagesList = generateList();
    private ArrayList<SetupDataCallbacks> mListeners = new ArrayList<SetupDataCallbacks>();
    private PagesList mPageList;
    private int mCurrentPageIndex = 0;
    private boolean mIsResumed = false;
    private boolean mIsFinished = false;
    private OnResumeRunnable mOnResumeRunnable;
    /**
     * CM SETUP WIZARD DATA CLASS
     */

    private boolean mTimeSet = false;
    private boolean mTimeZoneSet = false;
    private boolean mMobileDataEnabled;

    public PagesData(Context context) {
        mContext = context;
        mPageList = onNewPageList();
        mMobileDataEnabled = SetupWizardUtils.isMobileDataEnabled(context);
    }

    private PagesList generateList() {

    }

    @Override
    public void onPageLoaded(Page page) {
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onPageLoaded(page);
        }
    }

    @Override
    public void onPageTreeChanged() {
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onPageTreeChanged();
        }
    }

    @Override
    public void onFinish() {
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onFinish();
        }
    }

    @Override
    public void finishSetup() {
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).finishSetup();
        }
    }

    @Override
    public Page getPage(String key) {
        return mPageList.getPage(key);
    }

    @Override
    public Page getPage(int index) {
        return mPageList.getPage(index);
    }

    public Page getCurrentPage() {
        return mPageList.getPage(mCurrentPageIndex);
    }

    @Override
    public boolean isCurrentPage(Page page) {
        if (page == null) return false;
        return page.getKey().equals(getCurrentPage().getKey());
    }

    public boolean isFirstPage() {
        return mCurrentPageIndex == 0;
    }

    public boolean isLastPage() {
        return mCurrentPageIndex == mPageList.size() - 1;
    }

    @Override
    public void onNextPage() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (getCurrentPage().doNextAction() == false) {
                    if (advanceToNextUnhidden()) {
                        for (int i = 0; i < mListeners.size(); i++) {
                            mListeners.get(i).onNextPage();
                        }
                    }
                }
            }
        };
        doPreviousNext(runnable);
    }

    @Override
    public void onPreviousPage() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (getCurrentPage().doPreviousAction() == false) {
                    if (advanceToPreviousUnhidden()) {
                        for (int i = 0; i < mListeners.size(); i++) {
                            mListeners.get(i).onPreviousPage();
                        }
                    }
                }
            }
        };
        doPreviousNext(runnable);
    }

    private boolean advanceToNextUnhidden() {
        while (mCurrentPageIndex < mPageList.size()) {
            mCurrentPageIndex++;
            if (!getCurrentPage().isHidden()) {
                return true;
            }
        }
        return false;
    }

    private boolean advanceToPreviousUnhidden() {
        while (mCurrentPageIndex > 0) {
            mCurrentPageIndex--;
            if (!getCurrentPage().isHidden()) {
                return true;
            }
        }
        return false;
    }

    public void load(Bundle savedValues) {
        for (String key : savedValues.keySet()) {
            Page page = mPageList.getPage(key);
            if (page != null) {
                page.resetData(savedValues.getBundle(key));
            }
        }
    }

    private void doPreviousNext(Runnable runnable) {
        if (mIsResumed) {
            runnable.run();
        } else {
            mOnResumeRunnable = new OnResumeRunnable(runnable, this);
        }
    }

    public void onDestroy() {
        mOnResumeRunnable = null;
    }

    public void onPause() {
        mIsResumed = false;
    }

    public void onResume() {
        mIsResumed = true;
        if (mOnResumeRunnable != null) {
            mOnResumeRunnable.run();
        }
    }

    public void finishPages() {
        mIsFinished = true;
        for (Page page : mPageList.values()) {
            page.onFinishSetup();
        }
    }

    @Override
    public void addFinishRunnable(Runnable runnable) {
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).addFinishRunnable(runnable);
        }
    }

    public boolean isFinished() {
        return mIsFinished;
    }

    public Bundle save() {
        Bundle bundle = new Bundle();
        for (Page page : mPageList.values()) {
            bundle.putBundle(page.getKey(), page.getData());
        }
        return bundle;
    }

    public void registerListener(SetupDataCallbacks listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(SetupDataCallbacks listener) {
        mListeners.remove(listener);
    }

    @Override
    protected PagesList onNewPageList() {
        ArrayList<Page> pages = new ArrayList<Page>();
        if (SetupWizardUtils.hasLeanback(mContext)) {
            pages.add(new BluetoothSetupPage(mContext, this));
        }
        pages.add(new WelcomePage(mContext, this));
        pages.add(new WifiSetupPage(mContext, this));
        if (SetupWizardUtils.hasTelephony(mContext)) {
            pages.add(new SimCardMissingPage(mContext, this)
                    .setHidden(isSimInserted()));
        }
        if (SetupWizardUtils.isMultiSimDevice(mContext)) {
            pages.add(new ChooseDataSimPage(mContext, this)
                    .setHidden(!allSimsInserted()));
        }
        if (SetupWizardUtils.hasTelephony(mContext)) {
            pages.add(new MobileDataPage(mContext, this)
                    .setHidden(!isSimInserted() || mMobileDataEnabled));
        }
        if (SetupWizardUtils.hasGMS(mContext)) {
            pages.add(new GmsAccountPage(mContext, this).setHidden(true));
        }
        if (!SetupWizardUtils.hasLeanback(mContext) &&
                SetupWizardUtils.isPackageInstalled(mContext,
                        mContext.getString(R.string.cm_account_package_name))) {
            pages.add(new CyanogenServicesPage(mContext, this).setHidden(true));
        }
        if (SetupWizardUtils.hasFingerprint(mContext) && SetupWizardUtils.isOwner()) {
            pages.add(new FingerprintSetupPage(mContext, this));
        }
        pages.add(new CyanogenSettingsPage(mContext, this));
        pages.add(new OtherSettingsPage(mContext, this));
        pages.add(new DateTimePage(mContext, this));
        pages.add(new FinishPage(mContext, this));
        return new PageList(pages.toArray(new SetupPage[pages.size()]));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TelephonyIntents.ACTION_SIM_STATE_CHANGED)) {
            showHideDataSimPage();
            showHideSimMissingPage();
            showHideMobileDataPage();
            updateWelcomePage();
        } else if (intent.getAction()
                .equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            showHideMobileDataPage();
            showHideAccountPages();
        } else if (intent.getAction()
                .equals(TelephonyIntents.ACTION_ANY_DATA_CONNECTION_STATE_CHANGED)) {
            showHideMobileDataPage();
            showHideAccountPages();
        } else if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED) ||
                intent.getAction().equals(TelephonyIntents.ACTION_NETWORK_SET_TIMEZONE)) {
            mTimeZoneSet = true;
            showHideDateTimePage();
        } else if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED) ||
                intent.getAction().equals(TelephonyIntents.ACTION_NETWORK_SET_TIME)) {
            mTimeSet = true;
            showHideDateTimePage();
        }
    }

    private void updateWelcomePage() {
        WelcomePage welcomePage = (WelcomePage) getPage(WelcomePage.TAG);
        if (welcomePage != null) {
            welcomePage.simChanged();
        }
    }

    public IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        if (SetupWizardUtils.hasTelephony(mContext)) {
            filter.addAction(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
            filter.addAction(TelephonyIntents.ACTION_ANY_DATA_CONNECTION_STATE_CHANGED);
        }
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(TelephonyIntents.ACTION_NETWORK_SET_TIME);
        filter.addAction(TelephonyIntents.ACTION_NETWORK_SET_TIMEZONE);
        return filter;
    }

    private static class OnResumeRunnable implements Runnable {

        private final AbstractSetupData mAbstractSetupData;
        private final Runnable mRunnable;

        private OnResumeRunnable(Runnable runnable, AbstractSetupData abstractSetupData) {
            mAbstractSetupData = abstractSetupData;
            mRunnable = runnable;
        }

        @Override
        public void run() {
            mRunnable.run();
            mAbstractSetupData.mOnResumeRunnable = null;
        }
    }

    private class PagesList extends LinkedHashMap<String, Page> {

        protected PagesList(Page... pages) {
            for (Page page : pages)
                put(page.getClass().getCanonicalName(), page); // TODO: page->name()
        }

        public Page getPage(String key) {
            return get(key);
        }

        public Page getPage(int pos) {
            return get(keySet().toArray()[pos]);
        }
    }


}
