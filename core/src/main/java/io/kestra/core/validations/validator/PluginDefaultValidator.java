package io.kestra.core.validations.validator;

import io.kestra.core.models.flows.PluginDefault;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import io.kestra.core.validations.PluginDefaultValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Singleton
@Introspected
public class PluginDefaultValidator implements ConstraintValidator<PluginDefaultValidation, PluginDefault> {
    @Override
    public boolean isValid(@Nullable PluginDefault value, @NonNull AnnotationValue<PluginDefaultValidation> annotationMetadata, @NonNull ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        List<String> violations = new ArrayList<>();

        if (value.getValues() == null) {
            violations.add("Null values map found");
            addConstraintViolation(context, violations);
            return false;
        }

        if (value.getType() == null) {
            violations.add("No type provided");
        }

        // Check if the "values" map is empty
        for (Map.Entry<String, Object> entry : value.getValues().entrySet()) {
            if (entry.getValue() == null) {
                violations.add("Null value found in values with key " + entry.getKey());
            }
        }

        if (!violations.isEmpty()) {
            addConstraintViolation(context, violations);

            return false;
        } else {

            return true;
        }
    }

    private static void addConstraintViolation(final ConstraintValidatorContext context,
                                               final List<String> violations) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Invalid Plugin Default: " + String.join(", ", violations))
            .addConstraintViolation();
    }
}
