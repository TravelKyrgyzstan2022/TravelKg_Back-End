package com.example.benomad.logger;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public enum LogMarkers {
    READ{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("READ");
        }
    },
    UPDATE{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("UPDATE");
        }
    },
    CREATE{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("CREATE");
        }
    },
    DELETE{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("DELETE");
        }
    },
    AUTH{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("AUTH");
        }
    },
    OTHER{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("OTHER");
        }
    };

    public Marker get(){
        return null;
    }
}
