PROJECT_NAME := sandbox
SUBCOMMANDS := coroutines
EXECUTABLE := build/install/$(PROJECT_NAME)/bin/$(PROJECT_NAME)
KOTLIN_FILES := $(shell find src/ -type f -name "*.kt")
GRADLE_FILES := $(wildcard *.gradle)

all: $(EXECUTABLE)

test:
	./gradlew test

$(EXECUTABLE): $(KOTLIN_FILES) $(GRADLE_FILES)
	./gradlew clean test installDist

define RUN_SUBCOMMAND
$(subcommand): build/install/$(PROJECT_NAME)/bin/$(PROJECT_NAME)
	build/install/$(PROJECT_NAME)/bin/$(PROJECT_NAME) $(subcommand)
endef
$(foreach subcommand,$(SUBCOMMANDS),$(eval $(RUN_SUBCOMMAND)))

clean:
	./gradlew clean

.PHONY: \
	all \
	$(foreach subcommand,$(SUBCOMMANDS),$(subcommand)) \
	clean
