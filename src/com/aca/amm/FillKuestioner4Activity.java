package com.aca.amm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

import static com.aca.amm.R.id.swiKuestioner3;
import static com.aca.amm.R.id.swiKuestioner4;
import static com.aca.amm.R.id.swiKuestioner5a;
import static com.aca.amm.R.id.swiKuestioner5b;
import static com.aca.amm.R.id.txtKuestioner4;
import static com.aca.amm.R.id.txtKuestioner5a;
import static com.aca.amm.R.id.txtKuestioner5b;
import static com.aca.amm.R.id.txtPenjelasan1;
import static com.aca.amm.R.id.txtPenjelasan2;
import static com.aca.amm.R.id.txtPenjelasan3;
import static com.aca.amm.R.id.txtPenjelasan4;

public class FillKuestioner4Activity extends BaseActivity {

    @Bind(R.id.btnHome)
    ImageView btnHome;
    @Bind(R.id.layTitle2)
    RelativeLayout layTitle2;
    @Bind(R.id.lblTitle)
    CustomTextViewBold lblTitle;
    @Bind(R.id.layHeader)
    RelativeLayout layHeader;
    @Bind(R.id.cbSetuju)
    CheckBox cbSetuju;
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
        setContentView(R.layout.activity_fill_kuestioner4);
        ButterKnife.bind(this);
        disableView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoad();
    }

    @OnClick(R.id.btnNext)
    public void btnNextClick (View v) {
        onFinish();
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


    private void disableView(){
        if (Scalar.isCompleteSppa(this, Long.parseLong(GeneralSetting.getParameterValue(Var.SPPAID)))) {
            UtilView.disableView(this, cbSetuju);
        }
    }


    private void onFinish() {
        if (!Scalar.isCompleteSppa(this, Long.parseLong(GeneralSetting.getParameterValue(Var.SPPAID)))) {
            if (!validate()) {
                Toast.makeText(this, R.string.message_validate_setuju, Toast.LENGTH_SHORT).show();
                return;
            }
            onSave();
        }
        setResult(RESULT_OK);
        finish();
    }

    private boolean validate(){
        return cbSetuju.isChecked();
    }


    private void onSave() {
        try {
            MedisafeKuestioner kuestioner = new Select()
                    .from(MedisafeKuestioner.class)
                    .where(MedisafeKuestioner_Table.SppaNo.eq(GeneralSetting.getParameterValue(Var.SPPAID)))
                    .querySingle();

            if (kuestioner == null)
                kuestioner = new MedisafeKuestioner();

            kuestioner.IsAgreed = true;
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

            cbSetuju.setChecked(kuestioner.IsAgreed);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
