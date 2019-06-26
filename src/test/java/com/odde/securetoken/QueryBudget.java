package com.odde.securetoken;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

class QueryBudget {

    private BudgetRepo budgetRepo;
    private List<Budget> budgets;

    public QueryBudget(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
        this.budgets = budgetRepo.findAllBudgets();
    }

    public double query(LocalDate start, LocalDate end) throws InvalidTimeRangeException {
        if (start.isAfter(end)) throw new InvalidTimeRangeException();

        double result = 0.0;
        LocalDate endOfMonth = start.withDayOfMonth(start.lengthOfMonth());

        while (endOfMonth.isBefore(end)) {
            result += calBudgets(start, endOfMonth);
            start = endOfMonth.plusDays(1);
            endOfMonth = endOfMonth.plusMonths(1);
            endOfMonth = endOfMonth.withDayOfMonth(endOfMonth.lengthOfMonth());
        }
        if (start.isBefore(end) || start.isEqual(end)) {
            result += calBudgets(start, end);
        }

        return result;
    }

    private int getBudgetPerMonth(LocalDate date) {
        for (Budget budget : budgets) {
            if (budget.date.equals(String.format("%d/%d", date.getYear(), date.getMonthValue()))) {
                return budget.amount;
            }
        }
        return 0;
    }

    private double calBudgets(LocalDate start, LocalDate endOfMonth) {
        int daysBetween = (int) ChronoUnit.DAYS.between(start, endOfMonth) + 1;
        int budgetPerMonth = getBudgetPerMonth(endOfMonth);
        return budgetPerMonth * getRatioPerMonth(daysBetween, endOfMonth.lengthOfMonth());
    }

    private double getRatioPerMonth(int daysBetween, int daysPerMonth) {
        return (double) daysBetween / daysPerMonth;
    }
}
