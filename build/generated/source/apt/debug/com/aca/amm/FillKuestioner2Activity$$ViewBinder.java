// Generated code from Butter Knife. Do not modify!
package com.aca.amm;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FillKuestioner2Activity$$ViewBinder<T extends com.aca.amm.FillKuestioner2Activity> implements ViewBinder<T> {
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
    view = finder.findRequiredView(source, 2131493147, "field 'swiKuestioner1'");
    target.swiKuestioner1 = finder.castView(view, 2131493147, "field 'swiKuestioner1'");
    view = finder.findRequiredView(source, 2131493154, "field 'txtKuestioner1'");
    target.txtKuestioner1 = finder.castView(view, 2131493154, "field 'txtKuestioner1'");
    view = finder.findRequiredView(source, 2131493155, "field 'swiKuestioner2a'");
    target.swiKuestioner2a = finder.castView(view, 2131493155, "field 'swiKuestioner2a'");
    view = finder.findRequiredView(source, 2131493156, "field 'txtKuestioner2a'");
    target.txtKuestioner2a = finder.castView(view, 2131493156, "field 'txtKuestioner2a'");
    view = finder.findRequiredView(source, 2131493157, "field 'swiKuestioner2b'");
    target.swiKuestioner2b = finder.castView(view, 2131493157, "field 'swiKuestioner2b'");
    view = finder.findRequiredView(source, 2131493158, "field 'txtKuestioner2b'");
    target.txtKuestioner2b = finder.castView(view, 2131493158, "field 'txtKuestioner2b'");
    view = finder.findRequiredView(source, 2131493159, "field 'swiKuestioner2c'");
    target.swiKuestioner2c = finder.castView(view, 2131493159, "field 'swiKuestioner2c'");
    view = finder.findRequiredView(source, 2131493160, "field 'txtKuestioner2c'");
    target.txtKuestioner2c = finder.castView(view, 2131493160, "field 'txtKuestioner2c'");
    view = finder.findRequiredView(source, 2131493161, "field 'swiKuestioner2d'");
    target.swiKuestioner2d = finder.castView(view, 2131493161, "field 'swiKuestioner2d'");
    view = finder.findRequiredView(source, 2131493162, "field 'txtKuestioner2d'");
    target.txtKuestioner2d = finder.castView(view, 2131493162, "field 'txtKuestioner2d'");
    view = finder.findRequiredView(source, 2131493163, "field 'swiKuestioner2e'");
    target.swiKuestioner2e = finder.castView(view, 2131493163, "field 'swiKuestioner2e'");
    view = finder.findRequiredView(source, 2131493164, "field 'txtKuestioner2e'");
    target.txtKuestioner2e = finder.castView(view, 2131493164, "field 'txtKuestioner2e'");
    view = finder.findRequiredView(source, 2131493165, "field 'swiKuestioner2f'");
    target.swiKuestioner2f = finder.castView(view, 2131493165, "field 'swiKuestioner2f'");
    view = finder.findRequiredView(source, 2131493166, "field 'txtKuestioner2f'");
    target.txtKuestioner2f = finder.castView(view, 2131493166, "field 'txtKuestioner2f'");
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
    target.swiKuestioner1 = null;
    target.txtKuestioner1 = null;
    target.swiKuestioner2a = null;
    target.txtKuestioner2a = null;
    target.swiKuestioner2b = null;
    target.txtKuestioner2b = null;
    target.swiKuestioner2c = null;
    target.txtKuestioner2c = null;
    target.swiKuestioner2d = null;
    target.txtKuestioner2d = null;
    target.swiKuestioner2e = null;
    target.txtKuestioner2e = null;
    target.swiKuestioner2f = null;
    target.txtKuestioner2f = null;
    target.btnPrev = null;
    target.btnNext = null;
    target.scrollView1 = null;
    target.lblFooterTitle = null;
    target.layFooter = null;
  }
}
