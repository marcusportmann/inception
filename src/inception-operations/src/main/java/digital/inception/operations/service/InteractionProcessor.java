package digital.inception.operations.service;

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionProcessingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The {@code InteractionProcessor} class encapsulates all the logic for processing interactions.
 *
 * @author Marcus Portmann
 */
@Component
public class InteractionProcessor {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(InteractionProcessor.class);

  /** Constructs a new {@code InteractionProcessor}. */
  public InteractionProcessor() {}

  /**
   * Process the interaction.
   *
   * @param interaction the interaction
   * @return the result of processing the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the interaction could not be processed
   */
  public InteractionProcessingResult processInteraction(Interaction interaction)
      throws InvalidArgumentException, ServiceUnavailableException {
    log.info(
        "Processing the interaction ("
            + interaction.getId()
            + ") with type ("
            + interaction.getType()
            + ")");

    return new InteractionProcessingResult(interaction.getId());
  }
}
