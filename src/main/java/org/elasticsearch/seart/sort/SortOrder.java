package org.elasticsearch.seart.sort;

/**
 * Created by xusiao on 2018/5/3.
 */
public enum SortOrder  {
    ASC {
        public String toString() {
            return "asc";
        }
    },
    DESC {
        public String toString() {
            return "desc";
        }
    };

    private SortOrder() {
    }

   }
