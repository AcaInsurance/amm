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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aca.util.UtilView.enableView;
import static com.aca.util.UtilView.validateEmptyViews;

public class FillKuestioner1Activity extends BaseActivity {

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
    @Bind(R.id.txtNamaPerusahaan1)
    EditText txtNamaPerusahaan1;
    @Bind(R.id.txtNoPolis1)
    EditText txtNoPolis1;
    @Bind(R.id.txtNamaPerusahaan2)
    EditText txtNamaPerusahaan2;
    @Bind(R.id.txtNoPolis2)
    EditText txtNoPolis2;
    @Bind(R.id.txtNamaPerusahaan3)
    EditText txtNamaPerusahaan3;
    @Bind(R.id.txtNoPolis3)
    EditText txtNoPolis3;
    @Bind(R.id.txtNamaPerusahaan4)
    EditText txtNamaPerusahaan4;
    @Bind(R.id.txtNoPolis4)
    EditText txtNoPolis4;
    @Bind(R.id.swiKuestioner2)
    Switch swiKuestioner2;
    @Bind(R.id.txtKuestioner2)
    EditText txtKuestioner2;
    @Bind(R.id.swiKuestioner3)
    Switch swiKuestioner3;
    @Bind(R.id.txtKuestioner3)
    EditText txtKuestioner3;
    @Bind(R.id.swiKuestioner4)
    Switch swiKuestioner4;
    @Bind(R.id.txtKuestioner4)
    EditText txtKuestioner4;
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

    private List<String> namaPerusahaanList;
    private List<String> polisList;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_kuestioner1);
        ButterKnife.bind(this);
        initView();
        initList();
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
    public void btnNextClick( View v ) {
        onNext();
    }

    @OnClick(R.id.btnPrev)
    public void btnBackClick( View v ) {
        onBack();
    }


    @Override
    public void onBackPressed() {
        onBack();
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            finish();
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

        Intent intent = new Intent(this, FillKuestioner2Activity.class);
        startActivityForResult(intent, Var.REQUEST_CODE_KUESTIONER);
    }

    private void initView() {
        swiKuestioner1.setChecked(false);
        swiKuestioner2.setChecked(false);
        swiKuestioner3.setChecked(false);
        swiKuestioner4.setChecked(false);

        enableView(txtNamaPerusahaan1, false);
        enableView(txtNamaPerusahaan2, false);
        enableView(txtNamaPerusahaan3, false);
        enableView(txtNamaPerusahaan4, false);
        enableView(txtNoPolis1, false);
        enableView(txtNoPolis2, false);
        enableView(txtNoPolis3, false);
        enableView(txtNoPolis4, false);

        enableView(txtKuestioner2, false);
        enableView(txtKuestioner3, false);
        enableView(txtKuestioner4, false);
    }


    private void initList() {
        namaPerusahaanList = new ArrayList<>();
        polisList = new ArrayList<>();
    }

    private void registerListener() {
        if (Scalar.isCompleteSppa(this, Long.parseLong(GeneralSetting.getParameterValue(Var.SPPAID))))
            return;

        swiKuestioner1.setOnCheckedChangeListener(firstKuestionerCheckedChange());
        swiKuestioner2.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner2));
        swiKuestioner3.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner3));
        swiKuestioner4.setOnCheckedChangeListener(kuestionerCheckedChange(txtKuestioner4));
    }

    private void disableView() {
        if (Scalar.isCompleteSppa(this, Long.parseLong(GeneralSetting.getParameterValue(Var.SPPAID))))
            UtilView.disableView(this,
                    swiKuestioner1,
                    swiKuestioner2,
                    swiKuestioner3,
                    swiKuestioner4,
                    txtKuestioner2,
                    txtKuestioner3,
                    txtKuestioner4,
                    txtNoPolis1,
                    txtNoPolis2,
                    txtNoPolis3,
                    txtNoPolis4,
                    txtNamaPerusahaan1,
                    txtNamaPerusahaan2,
                    txtNamaPerusahaan3,
                    txtNamaPerusahaan4);
    }

    private CompoundButton.OnCheckedChangeListener firstKuestionerCheckedChange() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
                enableView(txtNamaPerusahaan1, isChecked);
                enableView(txtNamaPerusahaan2, isChecked);
                enableView(txtNamaPerusahaan3, isChecked);
                enableView(txtNamaPerusahaan4, isChecked);
                enableView(txtNoPolis1, isChecked);
                enableView(txtNoPolis2, isChecked);
                enableView(txtNoPolis3, isChecked);
                enableView(txtNoPolis4, isChecked);
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener kuestionerCheckedChange( final EditText keterangan ) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
                enableView(keterangan, isChecked);
            }
        };
    }

    private boolean validate() {
        return
                validateEmptyViews(txtKuestioner2, txtKuestioner3, txtKuestioner4) &&
                (validateDataPolis(txtNamaPerusahaan1, txtNoPolis1) &&
                validateDataPolis(txtNamaPerusahaan2, txtNoPolis2) &&
                validateDataPolis(txtNamaPerusahaan3, txtNoPolis3) &&
                validateDataPolis(txtNamaPerusahaan4, txtNoPolis4)) &&
                isEmptyNamaPerusahaan();

    }

    private boolean validateDataPolis( EditText perusahaan, EditText polis ) {
        try {
            return (!perusahaan.getText().toString().isEmpty() && !polis.getText().toString().isEmpty()) ||
                    (perusahaan.getText().toString().isEmpty() && polis.getText().toString().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isEmptyNamaPerusahaan() {
        try {
            if (!txtNamaPerusahaan1.isEnabled())
                return true;

            return (
                    !txtNamaPerusahaan1.getText().toString().isEmpty() ||
                    !txtNamaPerusahaan2.getText().toString().isEmpty() ||
                    !txtNamaPerusahaan3.getText().toString().isEmpty() ||
                    !txtNamaPerusahaan4.getText().toString().isEmpty()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

            kuestioner.IsYesA1 = swiKuestioner1.isChecked();
            kuestioner.IsYesA2 = swiKuestioner2.isChecked();
            kuestioner.IsYesA3 = swiKuestioner3.isChecked();
            kuestioner.IsYesA4 = swiKuestioner4.isChecked();

            String perusahaan1 = txtNamaPerusahaan1.getText().toString().trim();
            String perusahaan2 = txtNamaPerusahaan2.getText().toString().trim();
            String perusahaan3 = txtNamaPerusahaan3.getText().toString().trim();
            String perusahaan4 = txtNamaPerusahaan4.getText().toString().trim();

            String polis1 = txtNoPolis1.getText().toString().trim();
            String polis2 = txtNoPolis2.getText().toString().trim();
            String polis3 = txtNoPolis3.getText().toString().trim();
            String polis4 = txtNoPolis4.getText().toString().trim();

            List<String> namaPerusahaanList = new ArrayList<String>();
            List<String> polisList = new ArrayList<String>();

            namaPerusahaanList.add("");
            namaPerusahaanList.add("");
            namaPerusahaanList.add("");
            namaPerusahaanList.add("");

            polisList.add("");
            polisList.add("");
            polisList.add("");
            polisList.add("");

            int counter = 0;
            if (!perusahaan1.isEmpty()) {
                namaPerusahaanList.set(0, perusahaan1);
                polisList.set(0, polis1);
                counter++;
            }
            if (!perusahaan2.isEmpty()) {
                namaPerusahaanList.set(counter, perusahaan2);
                polisList.set(counter, polis2);
                counter++;
            }
            if (!perusahaan3.isEmpty()) {
                namaPerusahaanList.set(counter, perusahaan3);
                polisList.set(counter, polis3);
                counter++;
            }
            if (!perusahaan4.isEmpty()) {
                namaPerusahaanList.set(counter, perusahaan4);
                polisList.set(counter, polis4);
            }


            kuestioner.NamaPerusahaan1 = namaPerusahaanList.get(0);
            kuestioner.NamaPerusahaan2 = namaPerusahaanList.get(1);
            kuestioner.NamaPerusahaan3 = namaPerusahaanList.get(2);
            kuestioner.NamaPerusahaan4 = namaPerusahaanList.get(3);

            kuestioner.NoPolis1 = polisList.get(0);
            kuestioner.NoPolis2 = polisList.get(1);
            kuestioner.NoPolis3 = polisList.get(2);
            kuestioner.NoPolis4 = polisList.get(3);

            kuestioner.KeteranganA2 = txtKuestioner2.getText().toString();
            kuestioner.KeteranganA3 = txtKuestioner3.getText().toString();
            kuestioner.KeteranganA4 = txtKuestioner4.getText().toString();

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

            swiKuestioner1.setChecked(kuestioner.IsYesA1);
            swiKuestioner2.setChecked(kuestioner.IsYesA2);
            swiKuestioner3.setChecked(kuestioner.IsYesA3);
            swiKuestioner4.setChecked(kuestioner.IsYesA4);

            txtNamaPerusahaan1.setText(kuestioner.NamaPerusahaan1);
            txtNamaPerusahaan2.setText(kuestioner.NamaPerusahaan2);
            txtNamaPerusahaan3.setText(kuestioner.NamaPerusahaan3);
            txtNamaPerusahaan4.setText(kuestioner.NamaPerusahaan4);

            txtNoPolis1.setText(kuestioner.NoPolis1);
            txtNoPolis2.setText(kuestioner.NoPolis2);
            txtNoPolis3.setText(kuestioner.NoPolis3);
            txtNoPolis4.setText(kuestioner.NoPolis4);

            txtKuestioner2.setText(kuestioner.KeteranganA2);
            txtKuestioner3.setText(kuestioner.KeteranganA3);
            txtKuestioner4.setText(kuestioner.KeteranganA4);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
