package pm.workout.helper.api;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
@JsonDeserialize(using = LocalDateTimeDeserializer.class)
public @interface JsonLocalDateTimeDeserializer {
}
