package com.swizbiz.password_translator;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.swizbiz.password_translator.utils.PasswordGenerator;
import com.swizbiz.password_translator.utils.PasswordHelper;

import java.util.Locale;

public class MainActivity extends Activity {
    private static final int PASSWORD_LENGTH_DEFAULT = 8;
    private TextView resultTextView;
    private TextView resultGeneratedTextView;
    private TextView lengthPasswordTextView;
    private EditText sourceEditView;
    private SeekBar lengthPasswordSeekBar;
    private View buttonCopy;
    private PasswordHelper passwordHelper;
    private ImageView quality;
    private TextView qualityTextView;
    private CompoundButton useUppercase;
    private CompoundButton useDigits;
    private CompoundButton useSpecialSymbols;
    private View buttonGenerate;
    private View buttonCopyGenerated;
    private int passwordLength = PASSWORD_LENGTH_DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.result_text);
        resultGeneratedTextView = findViewById(R.id.result_generated_text);
        lengthPasswordTextView = findViewById(R.id.length_password_text);
        sourceEditView = findViewById(R.id.source_text);
        lengthPasswordSeekBar = findViewById(R.id.length_password_seek_bar);
        buttonCopy = findViewById(R.id.button_copy);
        buttonCopyGenerated = findViewById(R.id.button_copy_generated);
        buttonGenerate = findViewById(R.id.button_generate);
        quality = findViewById(R.id.quality);
        qualityTextView = findViewById(R.id.quality_text);
        useUppercase = findViewById(R.id.check_uppercase);
        useDigits = findViewById(R.id.check_digits);
        useSpecialSymbols = findViewById(R.id.check_special_symbols);

        passwordHelper = new PasswordHelper(getResources().getStringArray(R.array.russians), getResources().getStringArray(R.array.latins));

        sourceEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resultTextView.setText(passwordHelper.convert(s));
                buttonCopy.setEnabled(s.length() > 0);
                int quality = passwordHelper.getQuality(s);
                MainActivity.this.quality.setImageLevel(quality * 1000);
                qualityTextView.setText(getResources().getStringArray(R.array.qualities)[quality]);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonCopy.setEnabled(false);

        lengthPasswordSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passwordLength = PASSWORD_LENGTH_DEFAULT + progress;
                String extraSymbol = getResources().getQuantityString(R.plurals.count_password, progress, progress);
                String sumSymbols = getResources().getQuantityString(R.plurals.count_password, passwordLength, passwordLength);
                String countPassword = String.format(Locale.getDefault(), "Длина: %d + %s = %s", PASSWORD_LENGTH_DEFAULT, extraSymbol, sumSymbols);
                lengthPasswordTextView.setText(countPassword);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String generatedPassword = PasswordGenerator.generate(passwordLength, useUppercase.isChecked(), useDigits.isChecked(), useSpecialSymbols.isChecked());
                resultGeneratedTextView.setText(generatedPassword);
                buttonCopyGenerated.setEnabled(generatedPassword.length() > 0);
            }
        });

        buttonCopyGenerated.setEnabled(false);
    }

    public void buttonClicked(View view) {
        TextView textView = null;
        switch (view.getId()) {
            case R.id.button_copy:
                textView = resultTextView;
                break;
            case R.id.button_copy_generated:
                textView = resultGeneratedTextView;
                break;
        }
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null && textView != null) {
            manager.setPrimaryClip(ClipData.newPlainText(
                    MainActivity.this.getString(R.string.clipboard_title),
                    textView.getText()));
            Toast.makeText(MainActivity.this, R.string.message_copied, Toast.LENGTH_SHORT).show();
        }

    }
}
