package com.aca.amm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aca.dal.Scalar;
import com.aca.dbflow.GeneralSetting;
import com.aca.dbflow.MedisafeKuestioner;
import com.aca.dbflow.MedisafeKuestioner_Table;
import com.aca.util.UtilView;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Select;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.data;
import static com.aca.util.UtilView.enableView;
import static com.aca.util.UtilView.validateEmptyViews;

public class FillKuestioner3Activity extends BaseActivity {

    @Bind(R.id.btnHome)
    ImageView btnHome;
    @Bind(R.id.layTitle2)
    RelativeLayout layTitle2;
    @Bind(R.id.lblTitle)
    CustomTextViewBold lblTitle;
    @Bind(R.id.layHeader)
    RelativeLayout layHeader;
    @Bind(R.id.swiKuestioner3)
    Switch swiKuestioner3;
    @Bind(R.id.txtPenjelasan1)
    EditText txtPenjelasan1;
    @Bind(R.id.txtPenjelasan2)
    EditText txtPenjelasan2;
    @Bind(R.id.txtPenjelasan3)
    EditText txtPenjelasan3;
    @Bind(R.id.txtPenjelasan4)
    EditText txtPenjelasan4;
    @Bind(R.id.swiKuestioner4)
    Switch swiKuestioner4;
    @Bind(R.id.txtKuestioner4)
    EditText txtKuestioner4;
    @Bind(R.id.swiKuestioner5a)
    Switch swiKuestioner5a;
    @Bind(R.id.txtKuestioner5a)
    EditText txtKuestioner5a;
    @Bind(R.id.swiKuestioner5b)
    Switch swiKuestioner5b;
    @Bind(R.id.txtKuestioner5b)
    EditText txtKuestioner5b;
    @Bind(R.id.btnPrev)
    Button btnPrev;
    @Bind(R.id.btnNext)
    Button btnNext;
    @Bind(R.id.scrollView1)
    ScrollView scrollView1;
    @Bind(R.id.lblFooterTitle)
    TextView lblFooterTitle;
    @Bind(R.id.layFooter)
    RelativeLayout layFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_kuestioner3);
        ButterKnife.bind(this);
        initView();
        disableView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoad();
    }

    @OnClick(R.id.btnNext)
    public void btnNextClick(View v) {
        onNext();
    }

    @OnClick(R.id.btnPrev)
    public void btnBackClick(View v) {
        onBack();
    }


    @Override
    public void onBackPressed() {
        onBack();
    }


    private void onBack() {
        finish();
    }

    private void onNext() {
        if (!Scalar.isCompleteSppa(this, Long.parseLong(GeneralSetting.getParameterValue(Var.SPPAID)))) {
            if (!validate()) {
                Toast.makeText(this, R.string.message_validate_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            onSave();
        }
        Intent intent = new Intent(this, FillKuestioner4Activity.class);
        startActivityForResult(intent, Var.REQUEST_CODE_KUESTIONER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void initView() {
        swiKuestioner3.setChecked(false);
        swiKuestioner4.setChecked(false);
        swiKuestioner5a.setChecked(false);
        swiKuestioner5b.setChecked(false);

        enableView(txtPenjelasan1, false);
        enableView(txtPenjelasan2, false);
        enableView(txtPenjelasan3, false);
        enableView(txtPenjelasan4, false);
        enableView(txtKuestioner4, false);
        enableView(txtKuestioner5a, false);
        enableView(txtKuestioner5b, false);
    }

    private void registerListener() {
        if (Scalar.isCompleteSppa(this, Long.parseLong(GeneralSetting.getParameterValue(Var.SPPAID))))
            return;

        swiKuestioner3.setOnCheckedChangeListener(thirdKuestionerCheckedChange());
        swiKuestioner4.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner4));
        swiKuestioner5a.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner5a));
        swiKuestioner5b.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner5b));
    }

    private void disableView(){
        if (Scalar.isCompleteSppa(this, Long.parseLong(GeneralSetting.getParameterValue(Var.SPPAID)))) {
            UtilView.disableView(this,
                    swiKuestioner3,
                    swiKuestioner4,
                    swiKuestioner5a,
                    swiKuestioner5b,
                    txtKuestioner4,
                    txtKuestioner5a,
                    txtKuestioner5b,
                    txtPenjelasan1,
                    txtPenjelasan2,
                    txtPenjelasan3,
                    txtPenjelasan4
            );
        }
    }


    private CompoundButton.OnCheckedChangeListener thirdKuestionerCheckedChange() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enableView(txtPenjelasan1, isChecked);
                enableView(txtPenjelasan2, isChecked);
                enableView(txtPenjelasan3, isChecked);
                enableView(txtPenjelasan4, isChecked);
            }
        };
    }


    private CompoundButton.OnCheckedChangeListener kuestionerCheckedChange(final EditText keterangan) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enableView(keterangan, isChecked);
            }
        };
    }

    private boolean validate() {
        return
                validateEmptyViews(
                        txtPenjelasan1,
                        txtPenjelasan2,
                        txtPenjelasan3,
                        txtPenjelasan4,
                        txtKuestioner4,
                        txtKuestioner5a,
                        txtKuestioner5b);
    }

    private void onSave() {
        try {
            MedisafeKuestioner kuestioner = new Select()
                    .from(MedisafeKuestioner.class)
                    .where(MedisafeKuestioner_Table.SppaNo.eq(GeneralSetting.getParameterValue(Var.SPPAID)))
                    .querySingle();

            if (kuestioner == null)
                kuestioner = new MedisafeKuestioner();

            kuestioner.SppaNo = GeneralSetting.getParameterValue(Var.SPPAID);

            kuestioner.IsYesB3 = swiKuestioner3.isChecked();
            kuestioner.IsYesB4 = swiKuestioner4.isChecked();
            kuestioner.IsYesB5i = swiKuestioner5a.isChecked();
            kuestioner.IsYesB5ii = swiKuestioner5b.isChecked();

            kuestioner.KeteranganB31 = txtPenjelasan1.getText().toString();
            kuestioner.KeteranganB32 = txtPenjelasan2.getText().toString();
            kuestioner.KeteranganB33 = txtPenjelasan3.getText().toString();
            kuestioner.KeteranganB34 = txtPenjelasan4.getText().toString();

            kuestioner.KeteranganB4 = txtKuestioner4.getText().toString();
            kuestioner.KeteranganB5i = txtKuestioner5a.getText().toString();
            kuestioner.KeteranganB5ii = txtKuestioner5b.getText().toString();

            kuestioner.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onLoad() {
        try {
            MedisafeKuestioner kuestioner = new Select()
                    .from(MedisafeKuestioner.class)
                    .where(MedisafeKuestioner_Table.SppaNo.eq(GeneralSetting.getParameterValue(Var.SPPAID)))
                    .querySingle();

            if (kuestioner == null)
                return;

            swiKuestioner3.setChecked(kuestioner.IsYesB3);
            swiKuestioner4.setChecked(kuestioner.IsYesB4);
            swiKuestioner5a.setChecked(kuestioner.IsYesB5i);
            swiKuestioner5b.setChecked(kuestioner.IsYesB5ii);

            txtPenjelasan1.setText(kuestioner.KeteranganB31);
            txtPenjelasan2.setText(kuestioner.KeteranganB32);
            txtPenjelasan3.setText(kuestioner.KeteranganB33);
            txtPenjelasan4.setText(kuestioner.KeteranganB34);

            txtKuestioner4.setText(kuestioner.KeteranganB4);
            txtKuestioner5a.setText(kuestioner.KeteranganB5i);
            txtKuestioner5b.setText(kuestioner.KeteranganB5ii);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
