// Generated code from Butter Knife. Do not modify!
package com.aca.amm;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class InfoOtomateActivity$$ViewBinder<T extends com.aca.amm.InfoOtomateActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492880, "field 'lblTitle'");
    target.lblTitle = finder.castView(view, 2131492880, "field 'lblTitle'");
    view = finder.findRequiredView(source, 2131493298, "field 'imgLogo'");
    target.imgLogo = finder.castView(view, 2131493298, "field 'imgLogo'");
  }

  @Override public void unbind(T target) {
    target.lblTitle = null;
    target.imgLogo = null;
  }
}
