package com.lightconf.admin.model.dataobj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    private static final long serialVersionUID = 1L;

    public LogExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimitStart(Integer limitStart) {
        this.limitStart = limitStart;
    }

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd = limitEnd;
    }

    public Integer getLimitEnd() {
        return limitEnd;
    }

    protected abstract static class GeneratedCriteria implements Serializable {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andConfKeyIsNull() {
            addCriterion("conf_key is null");
            return (Criteria) this;
        }

        public Criteria andConfKeyIsNotNull() {
            addCriterion("conf_key is not null");
            return (Criteria) this;
        }

        public Criteria andConfKeyEqualTo(String value) {
            addCriterion("conf_key =", value, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyNotEqualTo(String value) {
            addCriterion("conf_key <>", value, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyGreaterThan(String value) {
            addCriterion("conf_key >", value, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyGreaterThanOrEqualTo(String value) {
            addCriterion("conf_key >=", value, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyLessThan(String value) {
            addCriterion("conf_key <", value, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyLessThanOrEqualTo(String value) {
            addCriterion("conf_key <=", value, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyLike(String value) {
            addCriterion("conf_key like", value, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyNotLike(String value) {
            addCriterion("conf_key not like", value, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyIn(List<String> values) {
            addCriterion("conf_key in", values, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyNotIn(List<String> values) {
            addCriterion("conf_key not in", values, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyBetween(String value1, String value2) {
            addCriterion("conf_key between", value1, value2, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfKeyNotBetween(String value1, String value2) {
            addCriterion("conf_key not between", value1, value2, "confKey");
            return (Criteria) this;
        }

        public Criteria andConfDescIsNull() {
            addCriterion("conf_desc is null");
            return (Criteria) this;
        }

        public Criteria andConfDescIsNotNull() {
            addCriterion("conf_desc is not null");
            return (Criteria) this;
        }

        public Criteria andConfDescEqualTo(String value) {
            addCriterion("conf_desc =", value, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescNotEqualTo(String value) {
            addCriterion("conf_desc <>", value, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescGreaterThan(String value) {
            addCriterion("conf_desc >", value, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescGreaterThanOrEqualTo(String value) {
            addCriterion("conf_desc >=", value, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescLessThan(String value) {
            addCriterion("conf_desc <", value, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescLessThanOrEqualTo(String value) {
            addCriterion("conf_desc <=", value, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescLike(String value) {
            addCriterion("conf_desc like", value, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescNotLike(String value) {
            addCriterion("conf_desc not like", value, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescIn(List<String> values) {
            addCriterion("conf_desc in", values, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescNotIn(List<String> values) {
            addCriterion("conf_desc not in", values, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescBetween(String value1, String value2) {
            addCriterion("conf_desc between", value1, value2, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfDescNotBetween(String value1, String value2) {
            addCriterion("conf_desc not between", value1, value2, "confDesc");
            return (Criteria) this;
        }

        public Criteria andConfValueIsNull() {
            addCriterion("conf_value is null");
            return (Criteria) this;
        }

        public Criteria andConfValueIsNotNull() {
            addCriterion("conf_value is not null");
            return (Criteria) this;
        }

        public Criteria andConfValueEqualTo(String value) {
            addCriterion("conf_value =", value, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueNotEqualTo(String value) {
            addCriterion("conf_value <>", value, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueGreaterThan(String value) {
            addCriterion("conf_value >", value, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueGreaterThanOrEqualTo(String value) {
            addCriterion("conf_value >=", value, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueLessThan(String value) {
            addCriterion("conf_value <", value, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueLessThanOrEqualTo(String value) {
            addCriterion("conf_value <=", value, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueLike(String value) {
            addCriterion("conf_value like", value, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueNotLike(String value) {
            addCriterion("conf_value not like", value, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueIn(List<String> values) {
            addCriterion("conf_value in", values, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueNotIn(List<String> values) {
            addCriterion("conf_value not in", values, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueBetween(String value1, String value2) {
            addCriterion("conf_value between", value1, value2, "confValue");
            return (Criteria) this;
        }

        public Criteria andConfValueNotBetween(String value1, String value2) {
            addCriterion("conf_value not between", value1, value2, "confValue");
            return (Criteria) this;
        }

        public Criteria andOptTimeIsNull() {
            addCriterion("opt_time is null");
            return (Criteria) this;
        }

        public Criteria andOptTimeIsNotNull() {
            addCriterion("opt_time is not null");
            return (Criteria) this;
        }

        public Criteria andOptTimeEqualTo(Date value) {
            addCriterion("opt_time =", value, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptTimeNotEqualTo(Date value) {
            addCriterion("opt_time <>", value, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptTimeGreaterThan(Date value) {
            addCriterion("opt_time >", value, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("opt_time >=", value, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptTimeLessThan(Date value) {
            addCriterion("opt_time <", value, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptTimeLessThanOrEqualTo(Date value) {
            addCriterion("opt_time <=", value, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptTimeIn(List<Date> values) {
            addCriterion("opt_time in", values, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptTimeNotIn(List<Date> values) {
            addCriterion("opt_time not in", values, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptTimeBetween(Date value1, Date value2) {
            addCriterion("opt_time between", value1, value2, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptTimeNotBetween(Date value1, Date value2) {
            addCriterion("opt_time not between", value1, value2, "optTime");
            return (Criteria) this;
        }

        public Criteria andOptUserIsNull() {
            addCriterion("opt_user is null");
            return (Criteria) this;
        }

        public Criteria andOptUserIsNotNull() {
            addCriterion("opt_user is not null");
            return (Criteria) this;
        }

        public Criteria andOptUserEqualTo(String value) {
            addCriterion("opt_user =", value, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserNotEqualTo(String value) {
            addCriterion("opt_user <>", value, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserGreaterThan(String value) {
            addCriterion("opt_user >", value, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserGreaterThanOrEqualTo(String value) {
            addCriterion("opt_user >=", value, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserLessThan(String value) {
            addCriterion("opt_user <", value, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserLessThanOrEqualTo(String value) {
            addCriterion("opt_user <=", value, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserLike(String value) {
            addCriterion("opt_user like", value, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserNotLike(String value) {
            addCriterion("opt_user not like", value, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserIn(List<String> values) {
            addCriterion("opt_user in", values, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserNotIn(List<String> values) {
            addCriterion("opt_user not in", values, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserBetween(String value1, String value2) {
            addCriterion("opt_user between", value1, value2, "optUser");
            return (Criteria) this;
        }

        public Criteria andOptUserNotBetween(String value1, String value2) {
            addCriterion("opt_user not between", value1, value2, "optUser");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion implements Serializable {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}