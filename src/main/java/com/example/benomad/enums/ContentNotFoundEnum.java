package com.example.benomad.enums;

public enum ContentNotFoundEnum {
    USER{
        @Override
        public String toString() {
            return "User";
        }
    },
    ARTICLE{
        @Override
        public String toString() {
            return "Article";
        }
    },
    BLOG{
        @Override
        public String toString() {
            return "Blog";
        }
    },
    COMMENT{
        @Override
        public String toString() {
            return "Comment";
        }
    },
    PLACE{
        @Override
        public String toString() {
            return "Place";
        }
    }
}
