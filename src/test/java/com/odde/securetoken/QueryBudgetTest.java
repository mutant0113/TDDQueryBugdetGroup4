package com.odde.securetoken;

import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class QueryBudgetTest {

    private QueryBudget queryBudget;

    private List<Budget> budgets = new ArrayList<>();

    @Test
    public void budget_full_month() {
        addBudgets(new Budget("2019/1", 31));
        givenQueryBudget();
        LocalDate start = LocalDate.of(2019, 1, 1);
        LocalDate end = LocalDate.of(2019, 1, 31);
        assertEquals(31.0, queryBudget.query(start, end));
    }

    @Test
    public void budget_one_day() {
        addBudgets(new Budget("2019/1", 31));
        givenQueryBudget();
        LocalDate start = LocalDate.of(2019, 1, 1);
        LocalDate end = LocalDate.of(2019, 1, 1);
        assertEquals(1.0, queryBudget.query(start, end));
    }

    @Test
    public void budget_two_full_months() {
        addBudgets(new Budget("2019/1", 31));
        addBudgets(new Budget("2019/2", 28));
        givenQueryBudget();
        LocalDate start = LocalDate.of(2019, 1, 1);
        LocalDate end = LocalDate.of(2019, 2, 28);
        assertEquals(31.0 + 28.0, queryBudget.query(start, end));
    }

    @Test
    public void budget_two_partial_months() {
        addBudgets(new Budget("2019/1", 31));
        addBudgets(new Budget("2019/2", 28));
        givenQueryBudget();
        LocalDate start = LocalDate.of(2019, 1, 15);
        LocalDate end = LocalDate.of(2019, 2, 15);
        assertEquals(15.0 + 17.0, queryBudget.query(start, end));
    }

    @Test
    public void budget_three_months_without_budget_in_a_month() {
        addBudgets(new Budget("2019/2", 28));
        addBudgets(new Budget("2019/4", 30));

        givenQueryBudget();
        LocalDate start = LocalDate.of(2019, 2, 1);
        LocalDate end = LocalDate.of(2019, 4, 30);
        assertEquals(28.0 + 30.0, queryBudget.query(start, end));
    }

    private void givenQueryBudget() {
        BudgetRepo budgetRepo = Mockito.mock(BudgetRepo.class);
        when(budgetRepo.findAllBudgets()).thenReturn(budgets);
        queryBudget = new QueryBudget(budgetRepo);
    }

    private void addBudgets(Budget budget) {
        budgets.add(budget);
    }
}
