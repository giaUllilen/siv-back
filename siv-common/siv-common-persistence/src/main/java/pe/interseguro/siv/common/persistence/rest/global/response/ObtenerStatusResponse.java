package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerStatusResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("user_name")
	private String user_name;
	
	@JsonProperty("user_password")
	private String user_password;
	
	@JsonProperty("user_hash")
	private String user_hash;
	
	@JsonProperty("cal_color")
	private String cal_color;
	
	@JsonProperty("first_name")
	private String first_name;
	
	@JsonProperty("last_name")
	private String last_name;
	
	@JsonProperty("reports_to_id")
	private String reports_to_id;
	
	@JsonProperty("is_admin")
	private String is_admin;
	
	@JsonProperty("currency_id")
	private String currency_id;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("date_entered")
	private String date_entered;
	
	@JsonProperty("date_modified")
	private String date_modified;
	
	@JsonProperty("modified_user_id")
	private String modified_user_id;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("department")
	private String department;
	
	@JsonProperty("phone_home")
	private String phone_home;
	
	@JsonProperty("phone_mobile")
	private String phone_mobile;
	
	@JsonProperty("phone_work")
	private String phone_work;
	
	@JsonProperty("phone_other")
	private String phone_other;
	
	@JsonProperty("phone_fax")
	private String phone_fax;
	
	@JsonProperty("email1")
	private String email1;
	
	@JsonProperty("email2")
	private String email2;
	
	@JsonProperty("secondaryemail")
	private String secondaryemail;
	
	@JsonProperty("status")
	private String status;

	@JsonProperty("signature")
	private String signature;
	
	@JsonProperty("address_street")
	private String address_street;
	
	@JsonProperty("address_city")
	private String address_city;
	
	@JsonProperty("address_state")
	private String address_state;
	
	@JsonProperty("address_country")
	private String address_country;
	
	@JsonProperty("address_postalcode")
	private String address_postalcode;
	
	@JsonProperty("user_preferences")
	private String user_preferences;
	
	@JsonProperty("tz")
	private String tz;
	
	@JsonProperty("holidays")
	private String holidays;
	
	@JsonProperty("namedays")
	private String namedays;
	
	@JsonProperty("workdays")
	private String workdays;
	
	@JsonProperty("weekstart")
	private String weekstart;
	
	@JsonProperty("date_format")
	private String date_format;
	
	@JsonProperty("hour_format")
	private String hour_format;
	
	@JsonProperty("start_hour")
	private String start_hour;
	
	@JsonProperty("end_hour")
	private String end_hour;
	
	@JsonProperty("is_owner")
	private String is_owner;
	
	@JsonProperty("activity_view")
	private String activity_view;
	
	@JsonProperty("lead_view")
	private String lead_view;
	
	@JsonProperty("imagename")
	private String imagename;
	
	@JsonProperty("deleted")
	private String deleted;
	
	@JsonProperty("confirm_password")
	private String confirm_password;
	
	@JsonProperty("internal_mailer")
	private String internal_mailer;
	
	@JsonProperty("reminder_interval")
	private String reminder_interval;
	
	@JsonProperty("reminder_next_time")
	private String reminder_next_time;
	
	@JsonProperty("crypt_type")
	private String crypt_type;
	
	@JsonProperty("accesskey")
	private String accesskey;
	
	@JsonProperty("theme")
	private String theme;
	
	@JsonProperty("language")
	private String language;
	
	@JsonProperty("time_zone")
	private String time_zone;
	
	@JsonProperty("currency_grouping_pattern")
	private String currency_grouping_pattern;
	
	@JsonProperty("currency_decimal_separator")
	private String currency_decimal_separator;
	
	@JsonProperty("currency_grouping_separator")
	private String currency_grouping_separator;
	
	@JsonProperty("currency_symbol_placement")
	private String currency_symbol_placement;
	
	@JsonProperty("phone_crm_extension")
	private String phone_crm_extension;
	
	@JsonProperty("no_of_currency_decimals")
	private String no_of_currency_decimals;
	
	@JsonProperty("truncate_trailing_zeros")
	private String truncate_trailing_zeros;
	
	@JsonProperty("dayoftheweek")
	private String dayoftheweek;
	
	@JsonProperty("callduration")
	private String callduration;
	
	@JsonProperty("othereventduration")
	private String othereventduration;
	
	@JsonProperty("calendarsharedtype")
	private String calendarsharedtype;
	
	@JsonProperty("default_record_view")
	private String default_record_view;
	
	@JsonProperty("leftpanelhide")
	private String leftpanelhide;
	
	@JsonProperty("rowheight")
	private String rowheight;
	
	@JsonProperty("defaulteventstatus")
	private String defaulteventstatus;
	
	@JsonProperty("defaultactivitytype")
	private String defaultactivitytype;
	
	@JsonProperty("hidecompletedevents")
	private String hidecompletedevents;
	
	@JsonProperty("defaultcalendarview")
	private String defaultcalendarview;
	
	@JsonProperty("zona")
	private String zona;
	
	@JsonProperty("agencia")
	private String agencia;
	
	@JsonProperty("google_id")
	private String google_id;
	
	@JsonProperty("codigoagente")
	private String codigoagente;
}
