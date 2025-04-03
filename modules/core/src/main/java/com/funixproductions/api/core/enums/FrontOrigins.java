package com.funixproductions.api.core.enums;

import com.google.common.base.Strings;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum FrontOrigins {
   FUNIX_PRODUCTIONS_DASHBOARD(
           "funixproductions-dashboard",
           "Dashboard Funix Productions",
           "https://dashboard.funixproductions.com",
           "https://dev.dashboard.funixproductions.com"
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

   @Nullable
   public static FrontOrigins getRedirectAuthOrigin(@Nullable String origin) {
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
