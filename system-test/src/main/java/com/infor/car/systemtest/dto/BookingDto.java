package com.infor.car.systemtest.dto;

import com.infor.car.systemtest.model.Booking;
import com.infor.car.systemtest.service.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    @Min(value = 1)
    @Getter
    @Setter
    private Long customerId;

    @NotEmpty(message = "Plate Number can't be empty.")
    @Getter
    @Setter
    private String plateNum;

    @NotNull(message = "From-Time booking can't be empty.")
    @Getter
    @Setter
    private LocalDateTime fromDateTime;

    @NotNull(message = "To-Time booking can't be empty.")
    @Getter
    @Setter
    private LocalDateTime toDateTime;

    @Override
    public String toString() {
        return "BookingDto{"+ "plateNum='"+ plateNum + '\''
                + ", customerId='"+ customerId + '\''
                + ", fromDateTime='"+ fromDateTime.format(AppConstants.getFormatter()) + '\''
                + ", toDateTime='" + toDateTime.format(AppConstants.getFormatter()) + '}';
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(booking.getCustomer().getId(), booking.getCar().getPlateNum(), booking.getFromDateTime(), booking.getToDateTime());
    }

    public static List<BookingDto> mapToBookingDto(List<Booking> bookings) {
        List<BookingDto> bookingDtos = new ArrayList<>();
        bookings.stream().forEach(b -> bookingDtos.add(mapToBookingDto(b)));
        return bookingDtos;
    }
}
