package com.funixproductions.api.core.enums;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FrontOrigins {
   FUNIX_PRODUCTIONS_DASHBOARD(
           "funixproductions-dashboard",
           "Dashboard Funix Productions",
           "https://dashboard.funixproductions.com",
           "http://localhost:4200"
   ),
   PACIFISTA_PUBLIC_WEB(
           "pacifista-public-web",
           "Pacifista Web",
           "https://pacifista.fr",
           "http://localhost:4200"
   );

   private final String origin;
   private final String humanReadableOrigin;
   private final String domainProd;
   private final String domainDev;

   public static FrontOrigins getRedirectAuthOrigin(String origin) {
       if (Strings.isNullOrEmpty(origin)) {
           return null;
       }

       for (FrontOrigins frontOrigins : FrontOrigins.values()) {
           if (frontOrigins.getOrigin().equals(origin)) {
               return frontOrigins;
           }
       }
       return null;
   }
}
