// Generated code from Butter Knife. Do not modify!
package com.aca.amm;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FillLabbaikActivity$$ViewBinder<T extends com.aca.amm.FillLabbaikActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493202, "field 'lblJumlahHari'");
    target.lblJumlahHari = finder.castView(view, 2131493202, "field 'lblJumlahHari'");
    view = finder.findRequiredView(source, 2131493204, "field 'lblJumlahMinggu'");
    target.lblJumlahMinggu = finder.castView(view, 2131493204, "field 'lblJumlahMinggu'");
    view = finder.findRequiredView(source, 2131493201, "field 'spnPlan'");
    target.spnPlan = finder.castView(view, 2131493201, "field 'spnPlan'");
    view = finder.findRequiredView(source, 2131493207, "field 'txtPremiAlokasi'");
    target.txtPremiAlokasi = finder.castView(view, 2131493207, "field 'txtPremiAlokasi'");
    view = finder.findRequiredView(source, 2131493208, "field 'txtTotalIdr'");
    target.txtTotalIdr = finder.castView(view, 2131493208, "field 'txtTotalIdr'");
  }

  @Override public void unbind(T target) {
    target.lblJumlahHari = null;
    target.lblJumlahMinggu = null;
    target.spnPlan = null;
    target.txtPremiAlokasi = null;
    target.txtTotalIdr = null;
  }
}
