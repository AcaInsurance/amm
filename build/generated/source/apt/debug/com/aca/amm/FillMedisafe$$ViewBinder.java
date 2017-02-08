// Generated code from Butter Knife. Do not modify!
package com.aca.amm;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FillMedisafe$$ViewBinder<T extends com.aca.amm.FillMedisafe> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493213, "field 'btnViewKuestioner' and method 'btnViewKuestionerClick'");
    target.btnViewKuestioner = finder.castView(view, 2131493213, "field 'btnViewKuestioner'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnViewKuestionerClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131493209, "field 'spnGender1'");
    target.spnGender1 = finder.castView(view, 2131493209, "field 'spnGender1'");
    view = finder.findRequiredView(source, 2131493210, "field 'spnGender2'");
    target.spnGender2 = finder.castView(view, 2131493210, "field 'spnGender2'");
    view = finder.findRequiredView(source, 2131493211, "field 'spnGender3'");
    target.spnGender3 = finder.castView(view, 2131493211, "field 'spnGender3'");
    view = finder.findRequiredView(source, 2131493212, "field 'spnGender4'");
    target.spnGender4 = finder.castView(view, 2131493212, "field 'spnGender4'");
  }

  @Override public void unbind(T target) {
    target.btnViewKuestioner = null;
    target.spnGender1 = null;
    target.spnGender2 = null;
    target.spnGender3 = null;
    target.spnGender4 = null;
  }
}
