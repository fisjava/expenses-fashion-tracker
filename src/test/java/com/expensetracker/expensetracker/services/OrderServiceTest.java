
package com.expensetracker.expensetracker.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository);
    }

    @Test
    void getTotalExpensesReturnsMonthlyWeeklyAndYearlyTotals() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth =
                today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate firstDayOfWeek =
                today.with(TemporalAdjusters.previousOrSame(
                        java.time.DayOfWeek.MONDAY));
        LocalDate firstDayOfYear =
                today.with(TemporalAdjusters.firstDayOfYear());

        when(orderRepository.sumExpensesBetweenDates(firstDayOfMonth, today))
                .thenReturn(100.0);
        when(orderRepository.sumExpensesBetweenDates(firstDayOfWeek, today))
                .thenReturn(30.0);
        when(orderRepository.sumExpensesBetweenDates(firstDayOfYear, today))
                .thenReturn(500.0);

        Map<String, Double> totals = orderService.getTotalExpenses();

        assertEquals(100.0, totals.get("monthly"));
        assertEquals(30.0, totals.get("weekly"));
        assertEquals(500.0, totals.get("yearly"));
    }

    @Test
    void getTotalIncomesReturnsMonthlyWeeklyAndYearlyTotals() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth =
                today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate firstDayOfWeek =
                today.with(TemporalAdjusters.previousOrSame(
                        java.time.DayOfWeek.MONDAY));
        LocalDate firstDayOfYear =
                today.with(TemporalAdjusters.firstDayOfYear());

        when(orderRepository.sumIncomesBetweenDates(firstDayOfMonth, today))
                .thenReturn(200.0);
        when(orderRepository.sumIncomesBetweenDates(firstDayOfWeek, today))
                .thenReturn(50.0);
        when(orderRepository.sumIncomesBetweenDates(firstDayOfYear, today))
                .thenReturn(900.0);

        Map<String, Double> totals = orderService.getTotalIncomes();

        assertEquals(200.0, totals.get("monthly"));
        assertEquals(50.0, totals.get("weekly"));
        assertEquals(900.0, totals.get("yearly"));
    }

    @Test
    void getTotalIncomesByMonthReturnsAllTwelveMonths() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            when(orderRepository.countIncomesBetweenDates(
                    yearMonth.atDay(1), yearMonth.atEndOfMonth()))
                    .thenReturn(month == today.getMonthValue()
                            ? 250.0 : null);
        }

        Map<String, Double> totals = orderService.getTotalIncomesByMonth();

        assertNotNull(totals);
        assertEquals(12, totals.size());
        assertEquals(250.0, totals.get(today.getMonth().name()));

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            if (month != today.getMonthValue()) {
                assertEquals(0.0, totals.get(yearMonth.getMonth().name()));
            }
        }
    }

    @Test
    void getTotalExpensesByMonthReturnsAllTwelveMonths() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            when(orderRepository.countExpensesBetweenDates(
                    yearMonth.atDay(1), yearMonth.atEndOfMonth()))
                    .thenReturn(month == today.getMonthValue()
                            ? 150.0 : null);
        }

        Map<String, Double> totals = orderService.getTotalExpensesByMonth();

        assertNotNull(totals);
        assertEquals(12, totals.size());
        assertEquals(150.0, totals.get(today.getMonth().name()));

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            if (month != today.getMonthValue()) {
                assertEquals(0.0, totals.get(yearMonth.getMonth().name()));
            }
        }
    }

    @Test
    void deleteByIdDelegatesToRepository() {
        Long orderId = 1L;

        orderService.deleteById(orderId);

        verify(orderRepository).deleteById(orderId);
    }
}
