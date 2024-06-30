package tihonel.com.github.workpermit;

import org.hibernate.annotations.processing.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = "classpath:application.properties")
public class WorkPermitApplication {
	@Bean
	public DateTimeFormatter dateTimeFormatter(){
		return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	}

	@Bean
	public DateTimeFormatter dateFormatter(){
		return DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(new Locale("ru"));
	}

	@Bean
	public DateTimeFormatter dateFormatterForReport(){
		return DateTimeFormatter.ofPattern("dd.MM.yy");
	}

	public static void main(String[] args) {
		SpringApplication.run(WorkPermitApplication.class, args);
	}

}
