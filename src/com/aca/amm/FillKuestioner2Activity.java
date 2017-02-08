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
import static com.aca.amm.R.id.swiKuestioner2;
import static com.aca.amm.R.id.swiKuestioner3;
import static com.aca.amm.R.id.swiKuestioner4;
import static com.aca.amm.R.id.txtKuestioner2;
import static com.aca.amm.R.id.txtKuestioner3;
import static com.aca.amm.R.id.txtKuestioner4;
import static com.aca.amm.R.id.txtNamaPerusahaan1;
import static com.aca.amm.R.id.txtNamaPerusahaan2;
import static com.aca.amm.R.id.txtNamaPerusahaan3;
import static com.aca.amm.R.id.txtNamaPerusahaan4;
import static com.aca.amm.R.id.txtNoPolis1;
import static com.aca.amm.R.id.txtNoPolis2;
import static com.aca.amm.R.id.txtNoPolis3;
import static com.aca.amm.R.id.txtNoPolis4;
import static com.aca.util.UtilView.enableView;
import static com.aca.util.UtilView.validateEmptyViews;

public class FillKuestioner2Activity extends BaseActivity {

    @Bind(R.id.btnHome)
    ImageView btnHome;
    @Bind(R.id.layTitle2)
    RelativeLayout layTitle2;
    @Bind(R.id.lblTitle)
    CustomTextViewBold lblTitle;
    @Bind(R.id.layHeader)
    RelativeLayout layHeader;
    @Bind(R.id.swiKuestioner1)
    Switch swiKuestioner1;
    @Bind(R.id.txtKuestioner1)
    EditText txtKuestioner1;
    @Bind(R.id.swiKuestioner2a)
    Switch swiKuestioner2a;
    @Bind(R.id.txtKuestioner2a)
    EditText txtKuestioner2a;
    @Bind(R.id.swiKuestioner2b)
    Switch swiKuestioner2b;
    @Bind(R.id.txtKuestioner2b)
    EditText txtKuestioner2b;
    @Bind(R.id.swiKuestioner2c)
    Switch swiKuestioner2c;
    @Bind(R.id.txtKuestioner2c)
    EditText txtKuestioner2c;
    @Bind(R.id.swiKuestioner2d)
    Switch swiKuestioner2d;
    @Bind(R.id.txtKuestioner2d)
    EditText txtKuestioner2d;
    @Bind(R.id.swiKuestioner2e)
    Switch swiKuestioner2e;
    @Bind(R.id.txtKuestioner2e)
    EditText txtKuestioner2e;
    @Bind(R.id.swiKuestioner2f)
    Switch swiKuestioner2f;
    @Bind(R.id.txtKuestioner2f)
    EditText txtKuestioner2f;
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
        setContentView(R.layout.activity_fill_kuestioner2);
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
    public void btnNextClick (View v) {
        onNext();
    }

    @OnClick(R.id.btnPrev)
    public void btnBackClick (View v) {
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
        Intent intent = new Intent(this, FillKuestioner3Activity.class);
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
        swiKuestioner1.setChecked(false);
        swiKuestioner2a.setChecked(false);
        swiKuestioner2b.setChecked(false);
        swiKuestioner2c.setChecked(false);
        swiKuestioner2d.setChecked(false);
        swiKuestioner2e.setChecked(false);
        swiKuestioner2f.setChecked(false);

        enableView(txtKuestioner1, false);
        enableView(txtKuestioner2a, false);
        enableView(txtKuestioner2b, false);
        enableView(txtKuestioner2c, false);
        enableView(txtKuestioner2d, false);
        enableView(txtKuestioner2e, false);
        enableView(txtKuestioner2f, false);
    }

    private void registerListener() {
        if (Scalar.isCompleteSppa(this, Long.parseLong(GeneralSetting.getParameterValue(Var.SPPAID))))
            return;

        swiKuestioner1.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner1));
        swiKuestioner2a.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner2a));
        swiKuestioner2b.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner2b));
        swiKuestioner2c.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner2c));
        swiKuestioner2d.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner2d));
        swiKuestioner2e.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner2e));
        swiKuestioner2f.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner2f));
    }

    private void disableView(){
        if (Scalar.isCompleteSppa(this, Long.parseLong(GeneralSetting.getParameterValue(Var.SPPAID)))) {
            UtilView.disableView(this,
                    swiKuestioner1,
                    swiKuestioner2a,
                    swiKuestioner2b,
                    swiKuestioner2c,
                    swiKuestioner2d,
                    swiKuestioner2e,
                    swiKuestioner2f,
                    txtKuestioner1,
                    txtKuestioner2a,
                    txtKuestioner2b,
                    txtKuestioner2c,
                    txtKuestioner2d,
                    txtKuestioner2e,
                    txtKuestioner2f
                    );
        }
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
                        txtKuestioner1,
                        txtKuestioner2a,
                        txtKuestioner2b,
                        txtKuestioner2c,
                        txtKuestioner2d,
                        txtKuestioner2e,
                        txtKuestioner2f);
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

            kuestioner.IsYesB1 = swiKuestioner1.isChecked();
            kuestioner.IsYesB2i = swiKuestioner2a.isChecked();
            kuestioner.IsYesB2ii = swiKuestioner2b.isChecked();
            kuestioner.IsYesB2iii = swiKuestioner2c.isChecked();
            kuestioner.IsYesB2iv = swiKuestioner2d.isChecked();
            kuestioner.IsYesB2v = swiKuestioner2e.isChecked();
            kuestioner.IsYesB2vi = swiKuestioner2f.isChecked();

            kuestioner.KeteranganB1 = txtKuestioner1.getText().toString();
            kuestioner.KeteranganB2i = txtKuestioner2a.getText().toString();
            kuestioner.KeteranganB2ii = txtKuestioner2b.getText().toString();
            kuestioner.KeteranganB2iii = txtKuestioner2c.getText().toString();
            kuestioner.KeteranganB2iv = txtKuestioner2d.getText().toString();
            kuestioner.KeteranganB2v = txtKuestioner2e.getText().toString();
            kuestioner.KeteranganB2vi = txtKuestioner2f.getText().toString();

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

            swiKuestioner1.setChecked(kuestioner.IsYesB1);
            swiKuestioner2a.setChecked(kuestioner.IsYesB2i);
            swiKuestioner2b.setChecked(kuestioner.IsYesB2ii);
            swiKuestioner2c.setChecked(kuestioner.IsYesB2iii);
            swiKuestioner2d.setChecked(kuestioner.IsYesB2iv);
            swiKuestioner2e.setChecked(kuestioner.IsYesB2v);
            swiKuestioner2f.setChecked(kuestioner.IsYesB2vi);

            txtKuestioner1.setText(kuestioner.KeteranganB1);
            txtKuestioner2a.setText(kuestioner.KeteranganB2i);
            txtKuestioner2b.setText(kuestioner.KeteranganB2ii);
            txtKuestioner2c.setText(kuestioner.KeteranganB2iii);
            txtKuestioner2d.setText(kuestioner.KeteranganB2iv);
            txtKuestioner2e.setText(kuestioner.KeteranganB2v);
            txtKuestioner2f.setText(kuestioner.KeteranganB2vi);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
