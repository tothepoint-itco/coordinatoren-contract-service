package company.tothepoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import company.tothepoint.ContractApplication;
import company.tothepoint.model.bediende.Bediende;
import company.tothepoint.repository.BediendeRepository;
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
public class BediendeControllerTest {
    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BediendeRepository bediendeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private RestDocumentationResultHandler document;


    private List<Bediende> originalData;

    @Before
    public void setUp() {
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();

        originalData = bediendeRepository.findAll();
        bediendeRepository.deleteAll();
    }

    @After
    public void tearDown() {
        bediendeRepository.deleteAll();
        bediendeRepository.save(originalData);
    }

    @Test
    public void listBediendes() throws Exception {
        bediendeRepository.save(new Bediende("Kaj", "Van der Hallen", LocalDate.of(1993, 8, 29),"035691011"));
        bediendeRepository.save(new Bediende("Butrint", "Zymberi", LocalDate.of(1992, 3, 24),"035691011"));


        this.document.snippets(
                responseFields(
                        fieldWithPath("[].id").description("The bediende's unique identifier"),
                        fieldWithPath("[].voorNaam").description("The bediende's first name"),
                        fieldWithPath("[].familieNaam").description("The bediende's last name"),
                        fieldWithPath("[].geboorteDatum").description("The bediende's birthday"),
                        fieldWithPath("[].telefoonNummer").description("The bediende telephone number'")
                )
        );

        this.mockMvc.perform(
                get("/bediendes").accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getBediende() throws Exception {
        Bediende bediende = bediendeRepository.save(new Bediende("Kaj", "Van der Hallen", LocalDate.of(1993, 8, 29),"035691011"));

        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("The bediende's unique identifier"),
                        fieldWithPath("voorNaam").description("The bediende's first name"),
                        fieldWithPath("familieNaam").description("The bediende's last name"),
                        fieldWithPath("geboorteDatum").description("The bediende's birthday"),
                        fieldWithPath("telefoonNummer").description("The bediende's telephone number")

                )
        );

        this.mockMvc.perform(
                get("/bediendes/" + bediende.getId()).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void createBediende() throws Exception {
        Map<String, String> newBediende = new HashMap<>();
        newBediende.put("voorNaam", "Kaj");
        newBediende.put("familieNaam", "Van der Hallen");
        newBediende.put("geboorteDatum", "1993-08-29");
        newBediende.put("telefoonNummer", "035691011");

        ConstrainedFields fields = new ConstrainedFields(Bediende.class);

        this.document.snippets(
                requestFields(
                        fields.withPath("voorNaam").description("The bediende's first name"),
                        fields.withPath("familieNaam").description("The bediende's last name"),
                        fields.withPath("geboorteDatum").description("The bediende's birthday"),
                        fields.withPath("telefoonNummer").description("The bediende's telephone number")
                )
        );

        this.mockMvc.perform(
                post("/bediendes").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(newBediende)
                )
        ).andExpect(status().isCreated());
    }

    @Test
    public void updateBediende() throws Exception {
        Bediende originalBediende = bediendeRepository.save(new Bediende("Koen", "Van Loock", LocalDate.of(1992, 3, 24),"035691011"));

        Map<String, String> updatedBediende = new HashMap<>();
        updatedBediende.put("voorNaam", "Butrint");
        updatedBediende.put("familieNaam", "Zymberi");
        updatedBediende.put("geboorteDatum", "1992-03-24");
        updatedBediende.put("telefoonNummer", "035691011");

        ConstrainedFields fields = new ConstrainedFields(Bediende.class);

        this.document.snippets(
                requestFields(
                        fields.withPath("voorNaam").description("The bediende's first name"),
                        fields.withPath("familieNaam").description("The bediende's last name"),
                        fields.withPath("geboorteDatum").description("The bediende's birthday"),
                        fields.withPath("telefoonNummer").description("The bediende's telephone number")
                )
        );

        this.mockMvc.perform(
                put("/bediendes/" + originalBediende.getId()).contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(updatedBediende)
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
