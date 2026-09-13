build:
	./gradlew \
	core:clean auth:clean \
	core:assemble auth:assemble \
	-x test --refresh-dependencies

pods:
	./gradlew \
	all:assembleKsaypipXCFramework \
	all:podPublishXCFramework \
	-x test --refresh-dependencies

version:
	 ./gradlew version --no-daemon --console=plain -q

.PHONY: build pods version
