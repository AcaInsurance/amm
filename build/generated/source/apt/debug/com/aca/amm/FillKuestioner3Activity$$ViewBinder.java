// Generated code from Butter Knife. Do not modify!
package com.aca.amm;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FillKuestioner3Activity$$ViewBinder<T extends com.aca.amm.FillKuestioner3Activity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492879, "field 'btnHome'");
    target.btnHome = finder.castView(view, 2131492879, "field 'btnHome'");
    view = finder.findRequiredView(source, 2131492878, "field 'layTitle2'");
    target.layTitle2 = finder.castView(view, 2131492878, "field 'layTitle2'");
    view = finder.findRequiredView(source, 2131492880, "field 'lblTitle'");
    target.lblTitle = finder.castView(view, 2131492880, "field 'lblTitle'");
    view = finder.findRequiredView(source, 2131492877, "field 'layHeader'");
    target.layHeader = finder.castView(view, 2131492877, "field 'layHeader'");
    view = finder.findRequiredView(source, 2131493150, "field 'swiKuestioner3'");
    target.swiKuestioner3 = finder.castView(view, 2131493150, "field 'swiKuestioner3'");
    view = finder.findRequiredView(source, 2131493167, "field 'txtPenjelasan1'");
    target.txtPenjelasan1 = finder.castView(view, 2131493167, "field 'txtPenjelasan1'");
    view = finder.findRequiredView(source, 2131493168, "field 'txtPenjelasan2'");
    target.txtPenjelasan2 = finder.castView(view, 2131493168, "field 'txtPenjelasan2'");
    view = finder.findRequiredView(source, 2131493169, "field 'txtPenjelasan3'");
    target.txtPenjelasan3 = finder.castView(view, 2131493169, "field 'txtPenjelasan3'");
    view = finder.findRequiredView(source, 2131493170, "field 'txtPenjelasan4'");
    target.txtPenjelasan4 = finder.castView(view, 2131493170, "field 'txtPenjelasan4'");
    view = finder.findRequiredView(source, 2131493152, "field 'swiKuestioner4'");
    target.swiKuestioner4 = finder.castView(view, 2131493152, "field 'swiKuestioner4'");
    view = finder.findRequiredView(source, 2131493153, "field 'txtKuestioner4'");
    target.txtKuestioner4 = finder.castView(view, 2131493153, "field 'txtKuestioner4'");
    view = finder.findRequiredView(source, 2131493171, "field 'swiKuestioner5a'");
    target.swiKuestioner5a = finder.castView(view, 2131493171, "field 'swiKuestioner5a'");
    view = finder.findRequiredView(source, 2131493172, "field 'txtKuestioner5a'");
    target.txtKuestioner5a = finder.castView(view, 2131493172, "field 'txtKuestioner5a'");
    view = finder.findRequiredView(source, 2131493173, "field 'swiKuestioner5b'");
    target.swiKuestioner5b = finder.castView(view, 2131493173, "field 'swiKuestioner5b'");
    view = finder.findRequiredView(source, 2131493174, "field 'txtKuestioner5b'");
    target.txtKuestioner5b = finder.castView(view, 2131493174, "field 'txtKuestioner5b'");
    view = finder.findRequiredView(source, 2131492957, "field 'btnPrev' and method 'btnBackClick'");
    target.btnPrev = finder.castView(view, 2131492957, "field 'btnPrev'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnBackClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492958, "field 'btnNext' and method 'btnNextClick'");
    target.btnNext = finder.castView(view, 2131492958, "field 'btnNext'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnNextClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492885, "field 'scrollView1'");
    target.scrollView1 = finder.castView(view, 2131492885, "field 'scrollView1'");
    view = finder.findRequiredView(source, 2131492884, "field 'lblFooterTitle'");
    target.lblFooterTitle = finder.castView(view, 2131492884, "field 'lblFooterTitle'");
    view = finder.findRequiredView(source, 2131492883, "field 'layFooter'");
    target.layFooter = finder.castView(view, 2131492883, "field 'layFooter'");
  }

  @Override public void unbind(T target) {
    target.btnHome = null;
    target.layTitle2 = null;
    target.lblTitle = null;
    target.layHeader = null;
    target.swiKuestioner3 = null;
    target.txtPenjelasan1 = null;
    target.txtPenjelasan2 = null;
    target.txtPenjelasan3 = null;
    target.txtPenjelasan4 = null;
    target.swiKuestioner4 = null;
    target.txtKuestioner4 = null;
    target.swiKuestioner5a = null;
    target.txtKuestioner5a = null;
    target.swiKuestioner5b = null;
    target.txtKuestioner5b = null;
    target.btnPrev = null;
    target.btnNext = null;
    target.scrollView1 = null;
    target.lblFooterTitle = null;
    target.layFooter = null;
  }
}
