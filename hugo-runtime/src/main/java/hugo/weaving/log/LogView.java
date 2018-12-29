package hugo.weaving.log;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedList;

import hugo.weaving.R;

/**
 * Created by lixindong2 on 12/29/18.
 */

public class LogView extends LinearLayout {

    private View mClearButton;
    private View mCloseButton;
    private RecyclerView mRecyclerView;
    private ContentAdapter mAdapter;

    private Formatter<LogInfo> mFormatter = new AndroidStudioStyleFormatter();
    private LevelFilter mLevelFilter = new LevelFilter(Log.VERBOSE);
    private ContentFilter mContentFilter = new ContentFilter(mFormatter);

    private LogCache mLogCache = new LogCache(400);
    private LogCache mDisplayCache = new LogCache(400);
    private LinearLayoutManager mLinearLayoutManager;
    private EditText mFilterEditText;
    private Spinner mLevelSpinner;

    public LogView(Context context) {
        this(context, null);
    }

    public LogView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setFocusable(true);
        setFocusableInTouchMode(true);
        LayoutInflater.from(context).inflate(R.layout.log_view, this, true);
        initView();
    }

    private void initView() {
        mClearButton = findViewById(R.id.clear);
        mCloseButton = findViewById(R.id.close);
        mRecyclerView = findViewById(R.id.recyclerView);
        mFilterEditText = findViewById(R.id.filter);
        mLevelSpinner = findViewById(R.id.levelSpinner);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
            }
        });
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLog();
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ContentAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContentFilter.content(s.toString());
                notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mLevelFilter.level(Log.VERBOSE);
                        break;
                    case 1:
                        mLevelFilter.level(Log.DEBUG);
                        break;
                    case 2:
                        mLevelFilter.level(Log.INFO);
                        break;
                    case 3:
                        mLevelFilter.level(Log.WARN);
                        break;
                    case 4:
                        mLevelFilter.level(Log.ERROR);
                        break;
                    case 5:
                        mLevelFilter.level(Log.ASSERT);
                        break;
                }
                notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        mFilterEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) showKeyboard(mFilterEditText);
//            }
//        });
    }

    public void setLogCache(LogCache logCache) {
        mLogCache = logCache;
    }

    public void appendLog(@LogLevel int type, String tag, String message) {
        mLogCache.append(new LogInfo(type, tag, message));
        notifyDataSetChanged();
    }

    public void clearLog() {
        mLogCache.clear();
        notifyDataSetChanged();
    }

    public void updateData(LogCache logCache) {
        setLogCache(logCache);
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        boolean needScroll = mLinearLayoutManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 2;
        LogCache newCache = new LogCache(400);
        for (LogInfo info : mLogCache.list) {
            if (mContentFilter.filter(info) && mLevelFilter.filter(info))
                newCache.append(info);
        }
        DiffCallback callback = new DiffCallback(mDisplayCache, newCache);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        mDisplayCache.clear();
        mDisplayCache.list.addAll(newCache.list);
        result.dispatchUpdatesTo(mAdapter);
        if (needScroll || mLinearLayoutManager.isSmoothScrolling()) {
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    public static class LogCache {
        private final LinkedList<LogInfo> list = new LinkedList<>();
        private final int maxSize;

        public LogCache(int maxSize) {
            this.maxSize = maxSize;
        }

        public synchronized void append(LogInfo info) {
            if (list.size() == maxSize) {
                list.removeFirst();
            }
            list.addLast(info);
        }
        public LogInfo get(int i) {
            return i >= 0 && i < list.size() ? list.get(i) : null;
        }
        public void clear() {
            list.clear();
        }

        public int size() {
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.spice_item_log, parent, false));
            content = itemView.findViewById(R.id.content);
        }
        public void bind(LogInfo info) {
            content.setText(mFormatter.format(info));
        }
    }
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(getItem(position));
        }

        private LogInfo getItem(int position) {
            return mDisplayCache.get(position);
        }

        @Override
        public int getItemCount() {
            return mDisplayCache.size();
        }
    }

    private static void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public static class DiffCallback extends DiffUtil.Callback {
        private final LogCache mOldCache;
        private final LogCache mNewCache;

        public DiffCallback(LogCache oldCache, LogCache newCache) {
            this.mOldCache = oldCache;
            this.mNewCache = newCache;
        }

        @Override
        public int getOldListSize() {
            return mOldCache.list.size();
        }

        @Override
        public int getNewListSize() {
            return mNewCache.list.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldCache.get(oldItemPosition) == mNewCache.get(newItemPosition);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            LogInfo oldItem = mOldCache.get(oldItemPosition);
            LogInfo newItem = mNewCache.get(newItemPosition);
            return oldItem.createAt == newItem.createAt && oldItem.level == newItem.level
                    && oldItem.tag.equals(newItem.tag) && oldItem.message.equals(newItem.message);
        }
    }
}
