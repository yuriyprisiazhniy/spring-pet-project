package spring.petproject.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import spring.petproject.domain.Auditorium;
import spring.petproject.service.AuditoriumService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class AuditoriumServiceImpl implements AuditoriumService {
    private static final Logger logger = LoggerFactory.getLogger(AuditoriumServiceImpl.class);

    private final Set<Auditorium> auditoriums;

    public AuditoriumServiceImpl(Map<String, String> auditoriumProperties) {
        Set<Auditorium> parsedResult = new HashSet<>();
        try {
            parsedResult = parsePropertyMap(auditoriumProperties);
        } catch (Exception e) {
            logger.error("Unable to parse auditorium data", e);
        }
        auditoriums = parsedResult;
    }

    @Nonnull
    @Override
    public Set<Auditorium> getAll() {
        return new HashSet<>(auditoriums);
    }

    @Nullable
    @Override
    public Auditorium getByName(@Nonnull String name) {
        return auditoriums.stream().filter(auditorium -> name.equals(auditorium.getName()))
                .findAny().orElse(null);
    }

    @Nonnull
    private Set<Auditorium> parsePropertyMap(Map<String, String> props) {
        Set<Auditorium> parsedAuditorium = props.entrySet().stream().map(entry -> {
            String name = entry.getKey();
            String[] seatsInfo = entry.getValue().split(";");
            long numberOfSeats = Long.valueOf(seatsInfo[0]);
            Set<Long> vipSeats = Arrays.stream(seatsInfo[1].substring(1, seatsInfo[1].length() - 1).split(","))
                    .filter(s -> !StringUtils.isEmpty(s))
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());

            Auditorium auditorium = new Auditorium();
            auditorium.setName(name);
            auditorium.setNumberOfSeats(numberOfSeats);
            auditorium.setVipSeats(vipSeats);
            return auditorium;
        }).collect(Collectors.toSet());

        if (parsedAuditorium.isEmpty()) {
            logger.warn("Could not parse any auditorium!");
        } else {
            logger.info("Successfully parsed {} auditorium(s)", parsedAuditorium.size());
        }
        return parsedAuditorium;
    }

}
