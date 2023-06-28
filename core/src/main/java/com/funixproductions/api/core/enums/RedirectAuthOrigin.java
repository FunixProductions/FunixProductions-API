package com.funixproductions.api.core.enums;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedirectAuthOrigin {
   FUNIX_PRODUCTIONS_DASHBOARD("funixproductions-dashboard"),
   PACIFISTA_PUBLIC_WEB("pacifista-public-web");

   private final String origin;

   public static RedirectAuthOrigin getRedirectAuthOrigin(String origin) {
       if (Strings.isNullOrEmpty(origin)) {
           return null;
       }

       for (RedirectAuthOrigin redirectAuthOrigin : RedirectAuthOrigin.values()) {
           if (redirectAuthOrigin.getOrigin().equals(origin)) {
               return redirectAuthOrigin;
           }
       }
       return null;
   }
}
