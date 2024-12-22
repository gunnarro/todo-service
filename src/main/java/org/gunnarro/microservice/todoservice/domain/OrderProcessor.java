package org.gunnarro.microservice.todoservice.domain;

public class OrderProcessor {

    // Vi innfører følgende 4 ordretyper:
    // - ALM : er alm og cyp varer
    // - KJØL: er kjørevarer
    // - NARK: er er narkotiske varer
    // - VAREPRØVE - kan inneholde alle varetyper

    // Dette fører til at vi får ett 1:1 forhold mellom ordre og hvilket lager varer skal plukkes fra.
    // Videre tilrettelegger det for å fakturer 1:1



    // ordre state er sekvensielle og må utføres etterhverandre,
    // det er ikke tillat å hopper over states eller gå bakover, men noen får unntak.
    public void processOrderState() {
        // sjekk om operasjon på ordre er tillatt for order state
        // dvs man kan ikke utføre plukk når en ordre er i PAKK state, for eksempel.
        // man kan ikke starte PLUKK for veroverføring har blitt utført, osv.

        // sjekk vareoverføring
        // sjekk plukk
        // sjekk dobbeltkontroll
        // sjekk pakk
        // sjekk fakturering
        // sjekk vare utmelding
        // sjekk transport
        // sjekk varer utlevert
    }

    // ref https://www.baeldung.com/java-state-design-pattern
    public enum OrderState {
        NY {
            @Override
            public OrderState nextState() {
                return VAREOVERFORING;
            }
        },
        VAREOVERFORING {
            @Override
            public OrderState nextState() {
                return PLUKK;
            }
        },
        PLUKK {
            @Override
            public OrderState nextState() {
                return DOBBELT_KONTROLL;
            }
        },
        DOBBELT_KONTROLL {
            @Override
            public OrderState nextState() {
                return PAKK;
            }
        },
        PAKK {
            @Override
            public OrderState nextState() {
                return FAKTURERING;
            }
        },
        FAKTURERING {
            @Override
            public OrderState nextState() {
                return TRANSPORT;
            }
        },
        TRANSPORT {
            @Override
            public OrderState nextState() {
                return UTLEVERING;
            }
        },
        UTLEVERING {
            @Override
            public OrderState nextState() {
                return this;
            }
        };

        public abstract OrderState nextState();
    }
}
