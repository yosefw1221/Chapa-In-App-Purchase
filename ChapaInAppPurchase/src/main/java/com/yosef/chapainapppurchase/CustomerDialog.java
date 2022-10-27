package com.yosef.chapainapppurchase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Patterns;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yosef.chapainapppurchase.model.Customer;
import com.yosef.chapainapppurchase.utils.EncryptedKeyValue;

@SuppressLint("ViewConstructor")
public class CustomerDialog extends LinearLayout {
    final EditText firstNameI;
    final EditText lastNameI;
    final EditText emailI;

    @SuppressLint("SetTextI18n")
    public CustomerDialog(@NonNull Context context, @NonNull CustomerCallback customerCallback) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setPadding(80, 120, 80, 120);
        TextView title = new TextView(getContext());
        title.setText("Fill Your Information");
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setLayoutParams(params(0, 20));
        firstNameI = editText("First Name", InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        lastNameI = editText("Last Name", InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        emailI = editText("Email", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        firstNameI.setBackgroundResource(R.drawable.edit_text_shape);
        lastNameI.setBackgroundResource(R.drawable.edit_text_shape);
        emailI.setBackgroundResource(R.drawable.edit_text_shape);
        CheckBox remember = new CheckBox(getContext());
        remember.setText("Remember");
        remember.setChecked(true);
        remember.setLayoutParams(params(20, 0));
        InputFilter[] nameFilter = new InputFilter[2];
        nameFilter[0] = new InputFilter.LengthFilter(30);
        nameFilter[1] = (charSequence, i, i1, spanned, i2, i3) -> {
            StringBuilder builder = new StringBuilder();
            for (int c = i; c < i1; c++) {
                char val = charSequence.charAt(c);
                if (Character.isAlphabetic(val)) builder.append(val);
            }
            return i1 - i == builder.length() ? null : builder.toString();
        };
        firstNameI.setFilters(nameFilter);
        lastNameI.setFilters(nameFilter);
        Button continueBtn = new Button(getContext());
        continueBtn.setBackgroundResource(R.drawable.btn_shape);
        continueBtn.setText("Continue to Payment");
        continueBtn.setTextColor(Color.WHITE);
        continueBtn.setAllCaps(false);
        continueBtn.setLayoutParams(params(30, 20));
        addView(title);
        addView(firstNameI);
        addView(lastNameI);
        addView(emailI);
        addView(remember);
        addView(continueBtn);
        continueBtn.setOnClickListener(view -> {
            String fName = firstNameI.getText().toString();
            String lName = lastNameI.getText().toString();
            String email = emailI.getText().toString();
            if (validateInput(fName, lName, email)) {
                if (remember.isChecked()) saveCustomer();
                customerCallback.onSuccess(new Customer(fName.trim(), lName.trim(), email.trim()));
            }
        });
    }

    private EditText editText(String hint, int inputType) {
        EditText editText = new EditText(getContext());
        editText.setHint(hint);
        editText.setInputType(inputType);
        editText.setLayoutParams(params(10, 10));
        return editText;
    }

    private LayoutParams params(int top, int bottom) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(10, top, 10, bottom);
        return params;
    }

    private boolean validateInput(String fName, String lName, String email) {
        boolean valid = true;
        if (fName.trim().isEmpty() || fName.length() < 2 || fName.length() > 30) {
            firstNameI.setError("Invalid First Name!");
            valid = false;
        }
        if (lName.trim().isEmpty() || lName.length() < 2 || lName.length() > 30) {
            lastNameI.setError("Invalid Last Name!");
            valid = false;
        }
        if (email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailI.setError("Invalid Email!");
            valid = false;
        }
        return valid;
    }

    private void saveCustomer() {
        EncryptedKeyValue pref = new EncryptedKeyValue(getContext(), "CHAPA_IN_APP_PAYMENT");
        String customer = firstNameI.getText().toString() + "," + lastNameI.getText().toString() + "," + emailI.getText().toString();
        pref.putValue("customer", customer);
    }
}
