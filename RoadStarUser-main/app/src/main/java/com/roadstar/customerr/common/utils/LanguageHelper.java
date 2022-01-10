package com.roadstar.customerr.common.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by Muneeb on 6/3/2018.
 */

public class LanguageHelper {
    Context context ;

      public LanguageHelper(String languageToLoad, Context context ) {
            this.context = context;
          Locale locale = new Locale(languageToLoad);//Your language
          Locale.setDefault(locale);
          Configuration config = new Configuration();
          config.locale = locale;
          context.getResources().updateConfiguration(config,
                  context.getResources().getDisplayMetrics());

      }


}
