package per.goweii.wanandroid.module.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import per.goweii.actionbarex.common.ActionIconView;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.basic.utils.EditTextUtils;
import per.goweii.basic.utils.InputMethodUtils;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.databinding.ActivitySearchBinding;
import per.goweii.wanandroid.module.home.fragment.SearchHistoryFragment;
import per.goweii.wanandroid.module.home.fragment.SearchResultFragment;

/**
 * @author CuiZhen
 * @date 2019/5/18
 * GitHub: https://github.com/goweii
 */
public class SearchActivity extends BaseActivity {

    ActionIconView aiv_back;
    ActionIconView aiv_clear;
    ActionIconView aiv_search;
    EditText et_search;
    FrameLayout fl;
    private ActivitySearchBinding binding;
    private SearchHistoryFragment mSearchHistoryFragment;
    private SearchResultFragment mSearchResultFragment;
    private FragmentManager mFragmentManager;
    private boolean mIsResultPage = false;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initContentView() {
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        aiv_back = binding.ab.findViewById(R.id.aiv_back);
        aiv_clear = binding.ab.findViewById(R.id.aiv_clear);
        aiv_search = binding.ab.findViewById(R.id.aiv_search);
        et_search = binding.ab.findViewById(R.id.et_search);
        fl = binding.fl;
    }


    @Override
    protected void initViews() {
        aiv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsResultPage) {
                    showHistoryFragment();
                } else {
                    finish();
                }
            }
        });
        aiv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = et_search.getText().toString();
                search(key);
            }
        });
        aiv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                InputMethodUtils.hide(et_search);
                et_search.clearFocus();
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = et_search.getText().toString();
                if (TextUtils.isEmpty(key)) {
                    aiv_clear.setVisibility(View.INVISIBLE);
                    showHistoryFragment();
                    mSearchResultFragment.clear();
                } else {
                    aiv_clear.setVisibility(View.VISIBLE);
                }
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    String key = et_search.getText().toString();
                    search(key);
                    return true;
                }
                return false;
            }
        });
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment searchHistoryFragment = mFragmentManager.findFragmentByTag(SearchHistoryFragment.class.getName());
        if (searchHistoryFragment == null) {
            mSearchHistoryFragment = SearchHistoryFragment.create();
            transaction.add(R.id.fl, mSearchHistoryFragment, SearchHistoryFragment.class.getName());
        } else {
            mSearchHistoryFragment = (SearchHistoryFragment) searchHistoryFragment;
        }
        Fragment searchResultFragment = mFragmentManager.findFragmentByTag(SearchResultFragment.class.getName());
        if (searchResultFragment == null) {
            mSearchResultFragment = SearchResultFragment.create();
            transaction.add(R.id.fl, mSearchResultFragment, SearchResultFragment.class.getName());
        } else {
            mSearchResultFragment = (SearchResultFragment) searchResultFragment;
        }
        transaction.show(mSearchHistoryFragment);
        transaction.hide(mSearchResultFragment);
        transaction.commit();
    }

    @Override
    protected void bindData() {
    }

    @Override
    public void onBackPressed() {
        if (mIsResultPage) {
            showHistoryFragment();
        } else {
            super.onBackPressed();
        }
    }

    public void search(String key) {
        InputMethodUtils.hide(et_search);
        et_search.clearFocus();
        if (TextUtils.isEmpty(key)) {
            showHistoryFragment();
            mSearchResultFragment.clear();
        } else {
            EditTextUtils.setTextWithSelection(et_search, key);
            showResultFragment();
            mSearchHistoryFragment.addHistory(key);
            mSearchResultFragment.search(key);
        }
    }

    private void showHistoryFragment() {
        if (!mIsResultPage) return;
        mIsResultPage = false;
        FragmentTransaction t = mFragmentManager.beginTransaction();
        t.hide(mSearchResultFragment);
        t.show(mSearchHistoryFragment);
        t.commit();
    }

    private void showResultFragment() {
        if (mIsResultPage) return;
        mIsResultPage = true;
        FragmentTransaction t = mFragmentManager.beginTransaction();
        t.hide(mSearchHistoryFragment);
        t.show(mSearchResultFragment);
        t.commit();
    }
}
