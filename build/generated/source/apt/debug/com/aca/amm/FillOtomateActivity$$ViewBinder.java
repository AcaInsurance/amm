// Generated code from Butter Knife. Do not modify!
package com.aca.amm;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FillOtomateActivity$$ViewBinder<T extends com.aca.amm.FillOtomateActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493222, "field 'spnFlood'");
    target.spnFlood = finder.castView(view, 2131493222, "field 'spnFlood'");
    view = finder.findRequiredView(source, 2131493223, "field 'spnEq'");
    target.spnEq = finder.castView(view, 2131493223, "field 'spnEq'");
    view = finder.findRequiredView(source, 2131493139, "field 'swiSRCC'");
    target.swiSRCC = finder.castView(view, 2131493139, "field 'swiSRCC'");
    view = finder.findRequiredView(source, 2131493140, "field 'swiTS'");
    target.swiTS = finder.castView(view, 2131493140, "field 'swiTS'");
    view = finder.findRequiredView(source, 2131492880, "field 'lblTitle'");
    target.lblTitle = finder.castView(view, 2131492880, "field 'lblTitle'");
    view = finder.findRequiredView(source, 2131493224, "field 'swiBengkel'");
    target.swiBengkel = finder.castView(view, 2131493224, "field 'swiBengkel'");
  }

  @Override public void unbind(T target) {
    target.spnFlood = null;
    target.spnEq = null;
    target.swiSRCC = null;
    target.swiTS = null;
    target.lblTitle = null;
    target.swiBengkel = null;
  }
}
