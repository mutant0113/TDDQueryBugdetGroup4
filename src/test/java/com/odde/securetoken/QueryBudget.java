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

    public double query(LocalDate start, LocalDate end) {
        double result = 0.0;
        LocalDate endOfMonth = start.withDayOfMonth(start.lengthOfMonth());

        while (endOfMonth.isBefore(end)) {
            int daysBetweenFirstMonth = (int) ChronoUnit.DAYS.between(start, endOfMonth) + 1;
            result += calBudgets(getBudgetPerMonth(endOfMonth), daysBetweenFirstMonth, endOfMonth.lengthOfMonth());
            start = endOfMonth.plusDays(1);
            endOfMonth = endOfMonth.plusMonths(1);
        }
        if (start.isBefore(end) || start.isEqual(end)) {
            int daysBetweenFirstMonth = (int) ChronoUnit.DAYS.between(start, end) + 1;
            result += calBudgets(getBudgetPerMonth(end), daysBetweenFirstMonth, end.lengthOfMonth());
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

    private double calBudgets(int budgetPerMonth, int daysBetween, int daysPerMonth) {
        return budgetPerMonth * getRatioPerMonth(daysBetween, daysPerMonth);
    }

    private double getRatioPerMonth(int daysBetween, int daysPerMonth) {
        return (double) daysBetween / daysPerMonth;
    }
}
