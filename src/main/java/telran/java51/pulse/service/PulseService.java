package telran.java51.pulse.service;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import telran.java51.pulse.dto.PulseDto;

@Configuration
@RequiredArgsConstructor
public class PulseService {
	final StreamBridge streamBridge;
	@Value("${lowPulse}")
	int minPulse;
	@Value("${highPulse}")
	int maxPulse = 110;

	@Bean
	Consumer<PulseDto> dispatchData() {
		return data -> {
			if (data.getPayLoad() < minPulse) {
				streamBridge.send("lowPulse-out-0", data);
				return;
			}
			if (data.getPayLoad() > maxPulse) {
				streamBridge.send("highPulse-out-0", data);
				return;
			}

			long delay = System.currentTimeMillis() - data.getTimeStamp();
			System.out.println("delay: " + delay + ", id: " + data.getId() + ",pulse: " + data.getPayLoad());
		};
	}
}
