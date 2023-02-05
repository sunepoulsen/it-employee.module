package dk.sunepoulsen.itemployee.stresstest

import dk.sunepoulsen.tes.docker.containers.TESBackendContainer
import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.utility.DockerImageName

@Slf4j
class DeploymentSpockExtension implements IGlobalExtension {
    private static final String PG_IMAGE_TAG = '15.1'
    private static final String PG_PASSWORD = 'itemployee_adm_jukilo90'

    private static TESBackendContainer itEmployeeModuleContainer = null
    private static GenericContainer postgresqlContainer = null

    static GenericContainer itEmployeeModuleContainer() {
        return itEmployeeModuleContainer
    }

    @Override
    void start() {
        DockerImageName imageName

        Network network = Network.newNetwork()

        imageName = DockerImageName.parse("postgres:${PG_IMAGE_TAG}")
        postgresqlContainer = new GenericContainer<>(imageName)
            .withEnv('POSTGRES_PASSWORD', PG_PASSWORD)
            .withExposedPorts(5432)
            .withFileSystemBind('../docker/scripts/postgres/001-create-it-employee-db.sql', '/docker-entrypoint-initdb.d/001-create-it-employee-db.sql', BindMode.READ_ONLY)
            .withNetwork(network)
            .withNetworkAliases('postgres')
        postgresqlContainer.start()

        itEmployeeModuleContainer = new TESBackendContainer('it-employee-backend-module', '1.0.0-SNAPSHOT', 'stress-test')
            .withConfigMapping('config/application-stress-test.yml')
            .dependsOn(postgresqlContainer)
            .withNetwork(network)
        itEmployeeModuleContainer.start()

        log.info('IT Employee Postgres Exported Port: {}', postgresqlContainer.getMappedPort(5432))
        log.info('IT Employee Module Exported Port: {}', itEmployeeModuleContainer.getMappedPort(8080))
    }

    @Override
    void visitSpec(SpecInfo spec) {
    }

    @Override
    void stop() {
        itEmployeeModuleContainer.copyLogFile('build/logs/it-employee-backend-module.log')
        itEmployeeModuleContainer.stop()

        postgresqlContainer.stop()
    }
}
