package io.yac.bankaccount.api.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yac.Application;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService;
import io.yac.auth.user.model.User;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.common.domain.SupportedCurrency;
import io.yac.transaction.domain.Transaction;
import net.minidev.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by geoffroy on 26/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BankAccountControllerIntegrationTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private BankAccountController bankAccountController;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bankAccountController).build();
    }


    @Test
    public void shouldGetById() throws Exception {
        final List<Transaction> transactions = new ArrayList<>();
        transactions.add(Transaction.builder().id(1L).build());
        transactions.add(Transaction.builder().id(2L).build());

        final User owner = User.builder().id(1L).build();
        final BankAccount bankAccount =
                BankAccount.builder().id(1L).name("Some name").currency(SupportedCurrency.CAD)
                        .transactions(transactions).owner(owner).build();

        CustomUserDetailsService.CurrentUser currentUser = new CustomUserDetailsService.CurrentUser(owner);
        when(bankAccountRepository.findOneByOwnerAndId(currentUser, 1L)).thenReturn(bankAccount);
        when(authenticationFacade.getCurrentUser()).thenReturn(currentUser);

        JSONArray expectedTransactions = new JSONArray();
        expectedTransactions.add(1);
        expectedTransactions.add(2);
        mockMvc.perform(get("/api/bankAccounts/" + bankAccount.getId()))
                .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
                .andExpect(jsonPath("$.currency").value(bankAccount.getCurrency().getExternalName()))
                .andExpect(jsonPath("$.name").value(bankAccount.getName()))
                .andExpect(jsonPath("$.transactions").value(expectedTransactions))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldGetById_no_data_found() throws Exception {
        CustomUserDetailsService.CurrentUser currentUser =
                new CustomUserDetailsService.CurrentUser(User.builder().id(1L).build());
        when(bankAccountRepository.findOneByOwnerAndId(currentUser, 1L)).thenReturn(null);
        when(authenticationFacade.getCurrentUser()).thenReturn(currentUser);

        mockMvc.perform(get("/api/bankAccounts/" + 1L))
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturn_all_the_account_of_the_current_user() throws Exception {
        final List<Transaction> transactions_account_1 = new ArrayList<>();
        transactions_account_1.add(Transaction.builder().id(1L).build());
        transactions_account_1.add(Transaction.builder().id(2L).build());

        final User owner = User.builder().id(1L).build();
        final BankAccount bankAccount_owner_1 =
                BankAccount.builder().id(1L).name("Some name").currency(SupportedCurrency.CAD)
                        .transactions(transactions_account_1).owner(owner).build();
        final BankAccount bankAccount_owner_2 =
                BankAccount.builder().id(2L).name("Some other name").currency(SupportedCurrency.EUR)
                        .transactions(new ArrayList<>()).owner(owner).build();


        CustomUserDetailsService.CurrentUser currentUser = new CustomUserDetailsService.CurrentUser(owner);
        when(bankAccountRepository.findByOwner(currentUser))
                .thenReturn(Arrays.asList(bankAccount_owner_1, bankAccount_owner_2));
        when(authenticationFacade.getCurrentUser()).thenReturn(currentUser);

        JSONArray expectedTransactions = new JSONArray();
        expectedTransactions.add(1);
        expectedTransactions.add(2);
        mockMvc.perform(get("/api/bankAccounts"))
                .andExpect(jsonPath("$[0].id").value(bankAccount_owner_1.getId().intValue()))
                .andExpect(jsonPath("$[0].currency").value(bankAccount_owner_1.getCurrency().getExternalName()))
                .andExpect(jsonPath("$[0].name").value(bankAccount_owner_1.getName()))
                .andExpect(jsonPath("$[0].transactions").value(expectedTransactions))
                .andExpect(jsonPath("$[1].id").value(bankAccount_owner_2.getId().intValue()))
                .andExpect(jsonPath("$[1].currency").value(bankAccount_owner_2.getCurrency().getExternalName()))
                .andExpect(jsonPath("$[1].name").value(bankAccount_owner_2.getName()))
                .andExpect(jsonPath("$[1].transactions").value(new JSONArray()))
                .andExpect(status().isOk());
    }


}
