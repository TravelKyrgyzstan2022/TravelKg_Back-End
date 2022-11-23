package com.example.benomad.logger;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public enum LogMarkers {
    READ{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("READ");
        }

        @Override
        public String toString() {
            return "READ";
        }
    },
    UPDATE{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("UPDATE");
        }

        @Override
        public String toString() {
            return "UPDATE";
        }
    },
    CREATE{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("CREATE");
        }

        @Override
        public String toString() {
            return "CREATE";
        }
    },
    DELETE{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("DELETE");
        }

        @Override
        public String toString() {
            return "DELETE";
        }
    },
    AUTH{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("AUTH");
        }

        @Override
        public String toString() {
            return "AUTH";
        }
    },
    OTHER{
        @Override
        public Marker get(){
            return MarkerFactory.getMarker("OTHER");
        }

        @Override
        public String toString() {
            return "OTHER";
        }
    };

    public Marker get(){
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
