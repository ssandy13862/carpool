package com.example.dec23_carpool.view.fragmentAll;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.view.MainActivity;

import java.util.LinkedList;
import java.util.List;

public class UserAddressFragment extends Fragment {

    private List<String> addressTitleList = new LinkedList<>();
    private List<String> addressTextList = new LinkedList<>();
    private RecyclerView.Adapter addressAdapter =
            new AddressAdapter(addressTitleList, addressTextList);
    private RecyclerView recyclerView;
    private TextView backTextView;
    private MainActivity mainActivity;
    private EditText addressEdit;
    private ImageButton modifyButton;

    public void setAddressItem(String addressTitle, String addressText) {
        addressTextList.add(addressTitle);
        addressTextList.add(addressText);
        addressAdapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_address,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        init();
    }

    private void findViews(@NonNull View view) {
        backTextView = view.findViewById(R.id.personalAddressBackTextView);
        addressEdit = view.findViewById(R.id.addressEdit);
        modifyButton = view.findViewById(R.id.userAddressModifyButton);
        recyclerView = view.findViewById(R.id.addressList);
    }

    private void init() {
        mainActivity = (MainActivity) getActivity();
        setBackView();
        setAddressEdit();
        setRecyclerViewAdapter();
    }

    @SuppressLint("SetTextI18n")
    private void setBackView() {
        backTextView.setText("<" + backTextView.getText().toString());
        backTextView.setOnClickListener(this::onBackTextViewClick);
    }

    private void onBackTextViewClick(View view) {
        mainActivity.switchToFragment(R.id.navigation_home);
    }

    private void setAddressEdit() {
        setEditTextEnable(true);
    }

    private void setEditTextEnable(boolean isEnable) {
        addressEdit.setFocusable(isEnable);
        addressEdit.setFocusableInTouchMode(isEnable);
        addressEdit.setLongClickable(isEnable);
        addressEdit.setInputType(isEnable ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_NULL);
    }

    private void setRecyclerViewAdapter() {
        recyclerView.setAdapter(addressAdapter);
    }

    private class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {

        private List<String> addressTitleList, addressTextList;

        AddressAdapter(List<String> addressTitleList,
                       List<String> addressTextList) {
            this.addressTitleList = addressTitleList;
            this.addressTextList = addressTextList;
        }

        @NonNull
        @Override
        public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AddressHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_user_address, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AddressHolder holder, int position) {
            String title = addressTitleList.get(position);
            String address = addressTextList.get(position);
            holder.setAddressContent(title, address);
        }

        @Override
        public int getItemCount() {
            return addressTitleList.size();
        }

        private class AddressHolder extends ViewHolder {

            TextView title, address;

            AddressHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.addressTitle);
                address = itemView.findViewById(R.id.addressText);
            }

            void setAddressContent(String title, String address) {
                this.title.setText(title);
                this.address.setText(address);
            }
        }
    }
}
