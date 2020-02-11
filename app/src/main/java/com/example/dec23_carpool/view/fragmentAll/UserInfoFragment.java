package com.example.dec23_carpool.view.fragmentAll;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.presenter.UserInfoFragmentPresenter;
import com.example.dec23_carpool.presenter.UserInfoFragmentPresenter.UserInfoFragmentView;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.view.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserInfoFragment extends Fragment implements UserInfoFragmentView {

    private RecyclerView recyclerView;
    private TextView backTextView;
    private ImageView modifyImage;
    private Button modifyCheckButton;
    private MainActivity mainActivity;
    private UserInfoFragmentPresenter userInfoFragmentPresenter;
    private User user;
    private List<EditText> editTextList;

    public UserInfoFragment(User user) {
        setUserInfo(user);
    }

    private void setUserInfo(User user) {
        setUserInfoList(user);
    }

    private void setUserInfoList(User user) {
        infoTextList.clear();
        infoTextList.add(user.getEmail());
        infoTextList.add(user.getPhone1());
        infoTextList.add(user.getNickname());
        infoTextList.add(user.getBirthday());
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        init();
    }

    private void findView(View view) {
        backTextView = view.findViewById(R.id.personalInfoBackTextView);
        recyclerView = view.findViewById(R.id.personalInfoList);
        modifyImage = view.findViewById(R.id.home_ic_edit);
        modifyCheckButton = view.findViewById(R.id.modifyCheckButton);
    }

    private String[] infoTitle = {"會員帳號", "手機號碼", "暱稱", "生日"};
    private List<String> infoTextList = new ArrayList<>(infoTitle.length);

    private void init() {
        mainActivity = (MainActivity) getActivity();
        userInfoFragmentPresenter = new UserInfoFragmentPresenter(
                this, Global.userRepository(), Global.threadExecutor());
        user = mainActivity.getUser();
        setBackView();
        setRecyclerView();
        setModifyImage();
        setModifyCheckButton();

    }

    @SuppressLint("SetTextI18n")
    private void setBackView() {
        backTextView.setText("<" + backTextView.getText().toString());
        backTextView.setOnClickListener(this::onBackTextViewClick);
    }

    private void onBackTextViewClick(View view) {
        mainActivity.switchToFragment(R.id.navigation_home);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter userInfoAdapter = new UserInfoAdapter(infoTitle, infoTextList);
        recyclerView.setAdapter(userInfoAdapter);
        recyclerView.addItemDecoration(new UserInfoItemDecoration(
                5, Color.parseColor("#c2c2d6")));
    }

    private void setModifyImage() {
        modifyImage.setOnClickListener(this::onModifyButtonClick);
    }

    private void setModifyCheckButton() {
        setModifyCheckButtonEnable(false);
        modifyCheckButton.setOnClickListener(this::onModifyCheckButtonClick);
    }

    private void onModifyButtonClick(View view) {
        UserInfoAdapter adapter = (UserInfoAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            editTextList = adapter.getEditTextList();
            if (editTextList != null) {
                setAllEditTextEnable(editTextList, true);
            }
        }
        setModifyCheckButtonEnable(true);
    }

    private void setModifyCheckButtonEnable(boolean isEnable) {
        modifyCheckButton.setClickable(isEnable);
        modifyCheckButton.setVisibility(isEnable ? View.VISIBLE : View.INVISIBLE);
    }

    private void onModifyCheckButtonClick(View view) {
        try {
            User u = (User) user.clone();
            u.setPhone1(editTextList.get(1).getText().toString());
            u.setNickname(editTextList.get(2).getText().toString());
            u.setBirthday(editTextList.get(3).getText().toString());
            userInfoFragmentPresenter.modifyUserInfo(u);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    private void setAllEditTextEnable(List<EditText> editTextList, boolean isEnable) {
        for (int editTextIndex = 0; editTextIndex < editTextList.size(); editTextIndex++) {
            EditText editText = editTextList.get(editTextIndex);
            if (editTextIndex != 0) {
                setEditTextEnable(editText, isEnable);
            }
        }
    }

    private void setEditTextEnable(EditText editText, boolean isEnable) {
        editText.setFocusable(isEnable);
        editText.setFocusableInTouchMode(isEnable);
        editText.setLongClickable(isEnable);
        editText.setInputType(isEnable ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_NULL);
    }

//    -------------------------------------------------------------------------------------------

    @Override
    public void onUpdateSuccessfully(User user) {
        this.user.setEmail(user.getEmail());
        this.user.setPhone1(user.getPhone1());
        this.user.setNickname(user.getNickname());
        this.user.setBirthday(user.getBirthday());

        mainActivity.setUser(user);
        setUserInfoList(user);
        setModifyCheckButtonEnable(false);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        UserInfoAdapter adapter = (UserInfoAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            editTextList = adapter.getEditTextList();
            if (editTextList != null) {
                setAllEditTextEnable(editTextList, false);
            }
        }
    }

    @Override
    public void onUpdateFail() {
        // Todo
    }

//    -------------------------------------------------------------------------------------------

    private class UserInfoAdapter extends Adapter<UserInfoAdapter.UserInfoViewHolder> {

        private String[] infoTitle;
        private List<String> infoTextList;
        private List<EditText> editTextList;

        UserInfoAdapter(String[] infoTitle, List<String> infoTextList) {
            this.infoTitle = infoTitle;
            this.infoTextList = infoTextList;
            editTextList = new ArrayList<>(infoTitle.length);
        }

        private List<EditText> getEditTextList() {
            return editTextList;
        }

        @NonNull
        @Override
        public UserInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.item_user_info, parent, false);
            UserInfoViewHolder userInfoViewHolder = new UserInfoViewHolder(view);
            editTextList.add(userInfoViewHolder.infoText);
            return userInfoViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserInfoViewHolder holder, int position) {
            holder.infoTitle.setText(infoTitle[position]);
            holder.infoText.setText(infoTextList.get(position));
        }

        @Override
        public int getItemCount() {
            return infoTitle.length;
        }

        private class UserInfoViewHolder extends RecyclerView.ViewHolder {
            TextView infoTitle;
            EditText infoText;

            UserInfoViewHolder(@NonNull View itemView) {
                super(itemView);
                infoTitle = itemView.findViewById(R.id.infoTitle);
                infoText = itemView.findViewById(R.id.infoText);
                setEditTextEnable(infoText, false);
            }

            EditText getInfoText() {
                return infoText;
            }
        }
    }

    private class UserInfoItemDecoration extends ItemDecoration {
        private int dividerSize;
        private Paint paint;

        UserInfoItemDecoration(int dividerSize, int dividerColor) {
            this.dividerSize = dividerSize;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setDither(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(dividerColor);
        }

        @Override
        public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull State state) {
            super.onDraw(c, parent, state);
            // 判斷是否為LinearLayoutManager
            if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                // 判斷LinearLayoutManager的方向，是否為VERTICAL
                if (!(((LinearLayoutManager) parent.getLayoutManager())
                        .getOrientation() == LinearLayoutManager.VERTICAL)) {
                    drawHorizontal(c, parent);
                }
            }
        }

        private void drawHorizontal(Canvas c, RecyclerView parent) {
            int count = parent.getChildCount();
            int top = parent.getPaddingTop();
            int bottom = parent.getHeight() - parent.getPaddingBottom();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int left = child.getRight() + params.rightMargin;
                int right = left + dividerSize;
                c.drawRect(left, top, right, bottom, paint);
            }
        }

        @Override
        public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int count = Objects.requireNonNull(parent.getAdapter()).getItemCount();
            int position = parent.getChildAdapterPosition(view);
            // 判斷是否為LinearLayoutManager
            if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                // 在判斷LinearLayoutManager的方向，是否為VERTICAL
                if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                    // 頂部 & 底部不繪製分隔線，所以如果不是最後1個Item就預留底部一些空間
                    if (position != count - 1) {
                        outRect.set(0, 0, 0, dividerSize);
                    }
                } else {
                    if (position != count - 1) {
                        outRect.set(0, 0, dividerSize, 0);
                    }
                }
            }
        }
    }
}
