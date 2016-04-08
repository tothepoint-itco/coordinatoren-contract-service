package company.tothepoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import company.tothepoint.ContractApplication;
import company.tothepoint.model.Contract;
import company.tothepoint.repository.ContractRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ContractApplication.class)
@WebAppConfiguration
public class ContractControllerTest {
    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private RestDocumentationResultHandler document;


    private List<Contract> originalData;

    @Before
    public void setUp() {
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();

        originalData = contractRepository.findAll();
        contractRepository.deleteAll();
    }

    @After
    public void tearDown() {
        contractRepository.deleteAll();
        contractRepository.save(originalData);
    }

    @Test
    public void listContracts() throws Exception {
        contractRepository.save(new Contract("ToThePoint"));
        contractRepository.save(new Contract("TBA"));

        this.document.snippets(
                responseFields(
                        fieldWithPath("[].id").description("The contract's unique identifier"),
                        fieldWithPath("[].naam").description("The contract's name")
                )
        );

        this.mockMvc.perform(
                get("/contracts").accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getContract() throws Exception {
        Contract contract = contractRepository.save(new Contract("ToThePoint"));

        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("The contract's unique identifier"),
                        fieldWithPath("naam").description("The contract's name")
                )
        );

        this.mockMvc.perform(
                get("/contracts/" + contract.getId()).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void createContract() throws Exception {
        Map<String, String> newContract = new HashMap<>();
        newContract.put("naam", "ToThePoint");

        ConstrainedFields fields = new ConstrainedFields(Contract.class);

        this.document.snippets(
                requestFields(
                        fields.withPath("naam").description("The contract's name")
                )
        );

        this.mockMvc.perform(
                post("/contracts").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(newContract)
                )
        ).andExpect(status().isCreated());
    }

    @Test
    public void updateContract() throws Exception {
        Contract originalContract = contractRepository.save(new Contract("Solution Architects"));

        Map<String, String> updatedContract = new HashMap<>();
        updatedContract.put("naam", "ToThePoint");

        ConstrainedFields fields = new ConstrainedFields(Contract.class);

        this.document.snippets(
                requestFields(
                        fields.withPath("naam").description("The contract's name")
                )
        );

        this.mockMvc.perform(
                put("/contract/" + originalContract.getId()).contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(updatedContract)
                )
        ).andExpect(status().isOk());
    }


    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }

}
