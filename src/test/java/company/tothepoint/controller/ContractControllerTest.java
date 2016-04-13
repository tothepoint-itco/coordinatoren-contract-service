package company.tothepoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import company.tothepoint.ContractApplication;
import company.tothepoint.model.bediende.Bediende;
import company.tothepoint.model.businessunit.BusinessUnit;
import company.tothepoint.model.contract.Contract;
import company.tothepoint.repository.BediendeRepository;
import company.tothepoint.repository.BusinessUnitRepository;
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


import java.time.LocalDate;
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
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private BediendeRepository bediendeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private RestDocumentationResultHandler document;


    private List<Contract> originalContractData;
    private List<BusinessUnit> originalBusinessUnitData;
    private List<Bediende> originalBediendeData;

    @Before
    public void setUp() {
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();

        originalContractData = contractRepository.findAll();
        contractRepository.deleteAll();
        originalBusinessUnitData = businessUnitRepository.findAll();
        businessUnitRepository.deleteAll();
        originalBediendeData = bediendeRepository.findAll();
        bediendeRepository.deleteAll();
    }

    @After
    public void tearDown() {
        contractRepository.deleteAll();
        contractRepository.save(originalContractData);
        businessUnitRepository.deleteAll();
        businessUnitRepository.save(originalBusinessUnitData);
        bediendeRepository.deleteAll();
        bediendeRepository.save(originalBediendeData);
    }

    @Test
    public void listContracts() throws Exception {
        new Contract("bed01", "bus01", LocalDate.of(2016, 04, 12));
        contractRepository.save(new Contract("bed01", "bus01", LocalDate.of(2016, 04, 12)));
        contractRepository.save(new Contract("bed02", "bus03", LocalDate.of(2016, 04, 10), LocalDate.of(2016, 05, 10)));

        this.document.snippets(
                responseFields(
                        fieldWithPath("[].id").description("The contract's unique identifier"),
                        fieldWithPath("[].bediendeId").description("The unique identifier of the bediende"),
                        fieldWithPath("[].businessUnitId").description("The unique identifier of the business unit"),
                        fieldWithPath("[].startDatum").description("The startdate of the contract"),
                        fieldWithPath("[].eindDatum").description("The enddate of the contract").optional().type(String.class)
                )
        );

        this.mockMvc.perform(
                get("/contracts").accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getContract() throws Exception {
        Contract contract = contractRepository.save(new Contract("bed02", "bus03", LocalDate.of(2016, 04, 10), LocalDate.of(2016, 05, 10)));

        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("The contract's unique identifier"),
                        fieldWithPath("bediendeId").description("The unique identifier of the bediende"),
                        fieldWithPath("businessUnitId").description("The unique identifier of the business unit"),
                        fieldWithPath("startDatum").description("The startdate of the contract"),
                        fieldWithPath("eindDatum").description("The enddate of the contract").optional()
                )
        );

        this.mockMvc.perform(
                get("/contracts/" + contract.getId()).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void createContract() throws Exception {
        BusinessUnit businessUnit = businessUnitRepository.save(new BusinessUnit("ToThePoint"));
        Bediende bediende = bediendeRepository.save(new Bediende("Kaj", "Van der Hallen", LocalDate.of(1993,8,29)));

        Map<String, String> newContract = new HashMap<>();
        newContract.put("bediendeId", bediende.getId());
        newContract.put("businessUnitId", businessUnit.getId());
        newContract.put("startDatum", "2016-04-12");

        ConstrainedFields fields = new ConstrainedFields(Contract.class);

        this.document.snippets(
                requestFields(
                        fields.withPath("bediendeId").description("The unique identifier of the bediende"),
                        fields.withPath("businessUnitId").description("The unique identifier of the business unit"),
                        fields.withPath("startDatum").description("The startdate of the contract"),
                        fields.withPath("eindDatum").description("The enddate of the contract").optional().type(String.class)
                )
        );

        this.mockMvc.perform(
                post("/contracts").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(newContract)
                )
        ).andExpect(status().isCreated());
    }

    @Test
    public void createContractWithInvalidBediendeIdShouldReturnBadRequest() throws Exception {
        BusinessUnit businessUnit = businessUnitRepository.save(new BusinessUnit("ToThePoint"));
        Map<String, String> newContract = new HashMap<>();
        newContract.put("bediendeId", "bed01");
        newContract.put("businessUnitId", businessUnit.getId());
        newContract.put("startDatum", "2016-04-12");

        this.mockMvc.perform(
                post("/contracts").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(newContract)
                )
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void createContractWithInvalidBusinessIdShouldReturnBadRequest() throws Exception {
        Bediende bediende = bediendeRepository.save(new Bediende("Kaj", "Van der Hallen", LocalDate.of(1993,8,29)));
        Map<String, String> newContract = new HashMap<>();
        newContract.put("bediendeId", bediende.getId());
        newContract.put("businessUnitId", "bus01");
        newContract.put("startDatum", "2016-04-12");

        this.mockMvc.perform(
                post("/contracts").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(newContract)
                )
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void updateContract() throws Exception {
        BusinessUnit businessUnit = businessUnitRepository.save(new BusinessUnit("ToThePoint"));
        Bediende bediende = bediendeRepository.save(new Bediende("Kaj", "Van der Hallen", LocalDate.of(1993,8,29)));

        Contract originalContract = contractRepository.save(new Contract(bediende.getId(), businessUnit.getId(), LocalDate.of(2016,4,12)));

        Map<String, String> updatedContract = new HashMap<>();
        updatedContract.put("bediendeId", bediende.getId());
        updatedContract.put("businessUnitId", businessUnit.getId());
        updatedContract.put("startDatum", "2016-04-14");

        ConstrainedFields fields = new ConstrainedFields(Contract.class);

        this.document.snippets(
                requestFields(
                        fields.withPath("bediendeId").description("The unique identifier of the bediende"),
                        fields.withPath("businessUnitId").description("The unique identifier of the business unit"),
                        fields.withPath("startDatum").description("The startdate of the contract"),
                        fields.withPath("eindDatum").description("The enddate of the contract").optional().type(String.class)
                )
        );

        this.mockMvc.perform(
                put("/contracts/" + originalContract.getId()).contentType(MediaType.APPLICATION_JSON).content(
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
