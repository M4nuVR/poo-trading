# Plataforma de Trading y Alertas de Inversión

> **Proyecto académico** · Java · Spring Boot · Programación Orientada a Objetos

Plataforma educativa para **simular cotizaciones de activos financieros**, calcular indicadores técnicos y generar alertas cuando se cumplan reglas de negocio configuradas. El proyecto está enfocado en aplicar los **cuatro pilares de la Programación Orientada a Objetos (POO)**, los principios **SOLID**, patrones de diseño y pruebas automatizadas.

> [!IMPORTANT]
> Este sistema utiliza información simulada y genera señales con fines educativos. **No ejecuta operaciones financieras ni proporciona asesoramiento de inversión.**

## Objetivo

Diseñar e implementar una aplicación modular que permita estudiar la colaboración entre objetos, el encapsulamiento del estado, el polimorfismo y la separación de responsabilidades, sin requerir una arquitectura de microservicios ni infraestructura distribuida.

## Funcionalidades previstas

- **Gestión de activos:** seguimiento de criptomonedas, divisas y acciones simuladas (mínimo cinco activos).
- **Simulación de mercado:** generación periódica de cotizaciones con precio y fecha de registro.
- **Historial:** consulta de cotizaciones almacenadas.
- **Análisis técnico:** cálculo de media móvil simple (**SMA**) y posibilidad de agregar otras estrategias, como **EMA**.
- **Reglas de alerta:** generación de señales simuladas al superar umbrales definidos.
- **Control de estados:** prevención de alertas duplicadas mientras persista una condición.
- **Notificaciones:** mensajes en consola y posibilidad de incorporar otros canales.
- **Visualización:** panel de precios y panel de alertas mediante una interfaz web sencilla.

## Casos de uso

El usuario de la plataforma educativa abre el panel para consultar la información del mercado simulado. La aplicación debe estar en ejecución y el acceso al panel es libre.

```mermaid
flowchart LR
    Usuario["«actor» Usuario de la plataforma educativa"]
    subgraph Plataforma["Plataforma educativa de trading"]
        CU01(["CU-01 · Consultar precios y estados"])
        CU02(["CU-02 · Consultar el historial de un activo"])
        CU03(["CU-03 · Consultar alertas recientes"])
    end
    Usuario --- CU01
    Usuario --- CU02
    Usuario --- CU03
```

| Caso de uso | Acción del usuario | Respuesta del panel | Endpoint |
| --- | --- | --- | --- |
| CU-01 | Abre la tabla **Precios**. | Muestra el código, el nombre, el tipo, el último precio y el estado de cada activo. Se actualiza cada tres segundos. | `GET /api/activos` |
| CU-02 | Selecciona la fila de un activo. | Muestra la hora y el precio de hasta 30 cotizaciones. Al abrir el panel, se selecciona el primer activo disponible. | `GET /api/activos/{codigo}/cotizaciones` |
| CU-03 | Revisa la sección **Alertas**. | Muestra hasta 20 señales recientes con su hora, código de activo y mensaje. Si la lista está vacía, indica «Sin alertas por ahora». | `GET /api/senales` |

La simulación genera las alertas automáticamente, incluso cuando el panel está cerrado. CU-03 permite consultar esas señales después de su emisión.

## Tecnologías

| Área | Tecnología |
| --- | --- |
| Lenguaje | Java 17 o superior |
| Backend | Spring Boot |
| Gestión de dependencias | Maven |
| Persistencia | Spring Data JPA + H2 (o PostgreSQL, opcional) |
| Pruebas | JUnit 5 y Mockito |
| Interfaz | HTML, CSS y JavaScript |

No es necesario utilizar Kafka, RabbitMQ, Docker, Kubernetes ni microservicios. La aplicación se ejecuta como **un único proyecto Spring Boot**.

## Enfoque de Programación Orientada a Objetos

| Concepto | Aplicación en el proyecto |
| --- | --- |
| **Encapsulamiento** | Las entidades protegen sus invariantes y ofrecen métodos como `registrarCotizacion()` o `marcarNotificada()` en lugar de setters indiscriminados. |
| **Abstracción** | Interfaces como `IndicadorTecnico`, `ProveedorPrecios`, `Notificador` y los repositorios separan los contratos de las implementaciones. |
| **Polimorfismo** | Es posible cambiar entre SMA, EMA u otros indicadores sin modificar el servicio que los utiliza. |
| **Herencia y composición** | Se prioriza la composición; la herencia se utiliza solamente cuando existe una relación válida de especialización. |
| **SOLID** | Cada componente mantiene una responsabilidad clara y depende de abstracciones. |

### Patrones de diseño

- **Strategy:** intercambiar algoritmos de indicadores técnicos.
- **State:** gestionar los estados del seguimiento y evitar alertas repetidas.
- **Repository:** desacoplar la lógica de dominio del almacenamiento.

Además se usa **inyección de dependencias por constructor** para entregar las implementaciones a los servicios.

## Arquitectura

La solución utiliza una arquitectura de **monolito modular**, organizada en cuatro capas:

```mermaid
flowchart TD
    UI[Interfaz web] --> WEB[Presentación: API REST]
    WEB --> APP[Aplicación: casos de uso]
    APP --> DOMAIN[Dominio: entidades, reglas y estados]
    APP --> PORTS[Interfaces de puertos]
    INFRA[Infraestructura: simulador, JPA y notificaciones] -. implementa .-> PORTS
    INFRA --> DB[(H2 / PostgreSQL)]
```

- **Dominio:** contiene las reglas financieras, entidades, objetos de valor y contratos necesarios para el comportamiento de los objetos.
- **Aplicación:** coordina los casos de uso sin asumir reglas que pertenecen al dominio.
- **Infraestructura:** implementa el simulador de precios, los repositorios y los canales de notificación.
- **Presentación:** ofrece endpoints REST y recursos web para consultar los resultados.

### Modelo de dominio

| Clase / interfaz | Responsabilidad |
| --- | --- |
| `ActivoFinanciero` | Identidad y datos de cada activo |
| `CodigoActivo`, `Precio` | Objetos de valor inmutables y validados |
| `Cotizacion` | Precio de un activo en un instante dado |
| `RastreadorActivo` | Historial reciente y estado del activo |
| `IndicadorTecnico` | Contrato de cálculo de indicadores |
| `MediaMovilSimple`, `MediaMovilExponencial` | Estrategias de indicadores |
| `ReglaDeAlerta` | Contrato de las condiciones para emitir señales |
| `ReglaPrecioSobreMedia` | Evalúa si el precio supera la media en un porcentaje |
| `EstadoSeguimiento`, `Vigilando`, `AlertaEmitida` | Comportamiento del seguimiento según su estado |
| `SenalInversion` | Resultado de una regla y estado de notificación |
| `ProveedorPrecios` | Contrato para obtener cotizaciones |
| `Notificador` | Contrato para informar una señal |
| `RepositorioCotizaciones`, `RepositorioSenales` | Contratos de persistencia |

### Diagrama de clases UML

Las tres vistas describen el mismo modelo Java. Una flecha continua indica una asociación, una flecha discontinua indica una dependencia y una línea discontinua con triángulo indica que una clase implementa una interfaz. El rombo sólido identifica una relación de composición.

#### 1. Modelo de dominio

Cada rastreador sigue un activo y conserva hasta 50 precios en memoria. Al registrar una cotización, verifica que el código coincida con el del activo, evalúa la regla con los precios anteriores y después incorpora el precio actual al historial. El repositorio guarda la cotización completa.

```mermaid
classDiagram
    direction TB
    class RastreadorActivo {
        -List~Double~ historial
        +validarPertenencia(Cotizacion cotizacion) void
        +registrarCotizacion(Cotizacion cotizacion, ReglaDeAlerta regla) Optional~SenalInversion~
        +getUltimoPrecio() Optional~Double~
        +getEstado() String
    }
    class ActivoFinanciero {
        -String nombre
        +getCodigo() CodigoActivo
        +getTipo() TipoActivo
        +getPrecioInicial() Precio
    }
    class Cotizacion {
        <<record>>
        +codigo() CodigoActivo
        +precio() Precio
        +fecha() LocalDateTime
    }
    class SenalInversion {
        -String mensaje
        -LocalDateTime fecha
        -boolean notificada
        +marcarNotificada() void
    }
    class CodigoActivo {
        <<record>>
        +valor() String
    }
    class Precio {
        <<record>>
        +valor() double
        +esMayorQue(double otro) boolean
    }
    class TipoActivo {
        <<enumeration>>
        CRIPTOMONEDA
        DIVISA
        ACCION
    }
    RastreadorActivo --> "1" ActivoFinanciero : sigue
    RastreadorActivo ..> Cotizacion : recibe
    RastreadorActivo ..> SenalInversion : crea si corresponde
    ActivoFinanciero --> "1" CodigoActivo : código
    ActivoFinanciero --> "1" Precio : precio inicial
    ActivoFinanciero --> "1" TipoActivo : tipo
    Cotizacion --> "1" CodigoActivo : código
    Cotizacion --> "1" Precio : precio observado
    SenalInversion --> "1" CodigoActivo : código
    SenalInversion --> "1" Precio : precio de la señal
```

#### 2. Reglas, indicadores y estados

La regla delega el cálculo de la media en un `IndicadorTecnico` (**Strategy**). El rastreador conserva un `EstadoSeguimiento` entre cotizaciones (**State**). Para emitir una señal deben cumplirse dos condiciones: la regla debe detectar que el precio supera el umbral y el estado debe permitir la emisión.

```mermaid
classDiagram
    direction TB
    class ReglaDeAlerta {
        <<interface>>
        +seCumple(List~Double~ preciosAnteriores, double precioActual) boolean
        +describir(double precioActual) String
    }
    class ReglaPrecioSobreMedia {
        -double porcentaje
    }
    class IndicadorTecnico {
        <<interface>>
        +nombre() String
        +calcular(List~Double~ precios) OptionalDouble
    }
    class MediaMovilSimple {
        -int periodo
    }
    class MediaMovilExponencial {
        -int periodo
    }
    class EstadoSeguimiento {
        <<interface>>
        +nombre() String
        +permiteAlerta() boolean
        +siguiente(boolean condicionCumplida) EstadoSeguimiento
    }
    RastreadorActivo ..> ReglaDeAlerta : evalúa
    RastreadorActivo --> "1" EstadoSeguimiento : mantiene
    ReglaPrecioSobreMedia ..|> ReglaDeAlerta
    ReglaPrecioSobreMedia --> "1" IndicadorTecnico : utiliza
    MediaMovilSimple ..|> IndicadorTecnico
    MediaMovilExponencial ..|> IndicadorTecnico
    Vigilando ..|> EstadoSeguimiento
    AlertaEmitida ..|> EstadoSeguimiento
```

#### 3. Servicio de mercado, puertos y adaptadores

`TareaSimulacion` dispara la actualización y `MercadoController` atiende las consultas. Ambos utilizan `ServicioMercado`, que crea un rastreador por código de activo. El servicio depende de interfaces para obtener precios, guardar cotizaciones, notificar y guardar señales. Los adaptadores JPA delegan el acceso a datos en los repositorios de Spring Data.

```mermaid
classDiagram
    direction TB
    class ServicioMercado {
        +actualizarMercado() void
        +resumen() List~ResumenActivo~
        +historial(String codigo) List~Cotizacion~
        +senales() List~SenalInversion~
    }
    class ReglaDeAlerta {
        <<interface>>
    }
    class ProveedorPrecios {
        <<interface>>
        +obtenerCotizacion(ActivoFinanciero activo) Cotizacion
    }
    class Notificador {
        <<interface>>
        +notificar(SenalInversion senal) void
    }
    class RepositorioCotizaciones {
        <<interface>>
        +guardar(Cotizacion cotizacion) void
        +buscarUltimas(CodigoActivo codigo, int cantidad) List~Cotizacion~
    }
    class RepositorioSenales {
        <<interface>>
        +guardar(SenalInversion senal) void
        +listarRecientes() List~SenalInversion~
    }
    TareaSimulacion --> ServicioMercado : actualiza
    MercadoController --> ServicioMercado : consulta
    ServicioMercado "1" *-- "0..*" RastreadorActivo : crea y conserva
    ServicioMercado --> ReglaDeAlerta : entrega al rastreador
    ServicioMercado --> ProveedorPrecios : solicita cotizaciones
    ServicioMercado --> Notificador : comunica señales
    ServicioMercado --> RepositorioCotizaciones : guarda y consulta
    ServicioMercado --> RepositorioSenales : guarda y consulta
    SimuladorPrecios ..|> ProveedorPrecios
    NotificadorConsola ..|> Notificador
    RepositorioCotizacionesJpa ..|> RepositorioCotizaciones
    RepositorioSenalesJpa ..|> RepositorioSenales
    RepositorioCotizacionesJpa --> CotizacionJpaRepository : delega
    RepositorioSenalesJpa --> SenalJpaRepository : delega
```

## Flujo principal

```mermaid
flowchart LR
    A[Simulador de precios] --> B[Registrar cotización]
    B --> C[Rastreador de activo]
    C --> D[Indicador técnico]
    D --> E[Regla de alerta]
    E --> F{¿Generar señal?}
    F -- Sí --> G[Notificar]
    G --> H[Guardar señal]
    F -- No --> I[Continuar seguimiento]
```

**Regla inicial:** si el precio actual supera en más del **5 %** la media móvil simple de las **cinco cotizaciones anteriores** (sin incluir la actual), se genera una señal educativa.

```math
SMA_5 = \frac{P_1 + P_2 + P_3 + P_4 + P_5}{5}
```

```math
PrecioActual > SMA_5 \times 1.05
```

La señal no se repite mientras se mantenga la condición. Para eso se usa el patrón State:

```mermaid
stateDiagram-v2
    [*] --> Vigilando
    Vigilando --> AlertaEmitida: se cumple la regla (emite señal)
    AlertaEmitida --> AlertaEmitida: se sigue cumpliendo (no emite)
    AlertaEmitida --> Vigilando: deja de cumplirse
```

## Organización del repositorio

```text
poo-trading/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/m4nuvr/trading/
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   ├── strategy/
│   │   │   │   ├── state/
│   │   │   │   ├── rules/
│   │   │   │   └── port/
│   │   │   ├── application/
│   │   │   ├── infrastructure/
│   │   │   │   ├── simulation/
│   │   │   │   ├── persistence/
│   │   │   │   ├── notification/
│   │   │   │   └── config/
│   │   │   ├── web/
│   │   │   └── TradingApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/java/com/m4nuvr/trading/
```

## Requisitos para ejecutar el proyecto

- **JDK 17 o superior** instalado y configurado.
- **Maven** instalado.
- Una terminal y un IDE compatible con Java (opcional).

### Puesta en marcha

1. Si recibiste el ZIP, descomprímelo y abre una terminal en la carpeta `poo-trading` que contiene el archivo `pom.xml`. Para obtener la versión publicada en GitHub, usa estos comandos.

   ```bash
   git clone https://github.com/M4nuVR/poo-trading.git
   cd poo-trading
   ```

2. Comprueba Java y Maven:

   ```bash
   java -version
   mvn -version
   ```

3. Ejecuta las pruebas:

   ```bash
   mvn test
   ```

4. Inicia la aplicación:

   ```bash
   mvn spring-boot:run
   ```

5. Abre `http://localhost:8080` para ver el panel. Los precios se actualizan cada 3 segundos y las alertas también se imprimen en consola.

### API REST

| Método | Ruta | Descripción |
| --- | --- | --- |
| GET | `/api/activos` | Activos con su último precio y estado de seguimiento |
| GET | `/api/activos/{codigo}/cotizaciones` | Últimas 30 cotizaciones de un activo |
| GET | `/api/senales` | Últimas 20 señales generadas |

La consola de H2 queda disponible en `/h2-console` (URL JDBC `jdbc:h2:mem:trading`, usuario `sa`).

## Pruebas

Las pruebas unitarias están escritas con **JUnit 5** y **Mockito** y se ejecutan con `mvn test`.

Casos verificados:

- Validación de precios positivos y códigos de activos correctos.
- Cálculo de SMA y otros indicadores.
- Comportamiento cuando no hay suficientes cotizaciones.
- Generación de señales ante el umbral configurado.
- Transiciones de estado y prevención de señales duplicadas.
- Intercambio de estrategias por polimorfismo.
- Colaboración entre servicios, repositorios y notificadores usando dobles de prueba.

Las pruebas del dominio se ejecutan **sin iniciar Spring Boot ni una base de datos real**.

### TDD aplicado al registro de cotizaciones

Si el servicio pide el precio de AAPL y el proveedor responde con el de BTC, debe rechazar esa cotización antes de guardarla, incluso cuando ambos activos estén registrados. La rama `tdd/validacion-cotizaciones` del ZIP conserva el desarrollo de este cambio.

| Etapa | Qué se comprobó | Resultado |
| --- | --- | --- |
| Base · `7eebce3` | Se ejecutaron las pruebas originales. | 19 aprobadas |
| Rojo · `a20da65` | En `rechazaCotizacionDeOtroActivoAntesDeGuardarla`, JUnit confirmó la excepción esperada. Mockito detectó que el servicio ya había llamado a `guardar`. | 20 aprobadas, 1 fallida |
| Verde · `bdd0e44` | El servicio comprobó el código del activo antes de guardar la cotización. | 21 aprobadas |
| Refactorización · `73e7c1a` | Se reunió la comprobación en `RastreadorActivo.validarPertenencia`. El servicio y el registro directo del rastreador usan ese método. | 21 aprobadas |

La segunda prueba nueva, `permiteReintentarLaAlertaSiFallaElGuardadoDeLaCotizacion`, protege el orden de las operaciones. Simula un fallo al guardar una cotización válida y comprueba que el último precio y el estado se conservan. Cuando el proveedor entrega la misma cotización en el siguiente intento y el guardado tiene éxito, se notifica y se guarda una sola señal.

Desde la carpeta que contiene `pom.xml`, ejecuta el caso principal y después todas las pruebas.

```bash
mvn "-Dtest=ServicioMercadoTest#rechazaCotizacionDeOtroActivoAntesDeGuardarla" test
mvn clean test
```

Para mostrar el fallo original, abre el commit rojo en una carpeta de trabajo independiente y ejecuta allí la misma prueba. En ese commit, el fallo esperado es `NoInteractionsWanted` sobre `repositorioCotizaciones`.

```bash
git worktree add --detach ../poo-trading-rojo a20da65
mvn -f ../poo-trading-rojo/pom.xml "-Dtest=ServicioMercadoTest#rechazaCotizacionDeOtroActivoAntesDeGuardarla" test
```

## Explicación del código

1. `TareaSimulacion` llama cada 3 segundos a `ServicioMercado.actualizarMercado()`.
2. El servicio pide una cotización nueva a `ProveedorPrecios` (implementado por `SimuladorPrecios`, que varía el precio hasta ±4 %). El rastreador comprueba que pertenece al activo solicitado y el servicio la guarda en `RepositorioCotizaciones`.
3. Cada activo tiene un `RastreadorActivo` que guarda su historial reciente y su estado (`Vigilando` o `AlertaEmitida`).
4. El rastreador consulta la `ReglaDeAlerta`. La regla usa un `IndicadorTecnico` (SMA o EMA) sin saber cuál es: eso es Strategy y polimorfismo.
5. Si la regla se cumple y el estado lo permite, se crea una `SenalInversion`, se envía al `Notificador` (consola) y se guarda en `RepositorioSenales`.
6. El dominio solo conoce interfaces (`domain/port`). La infraestructura (JPA, simulador, consola) las implementa, y `ConfiguracionTrading` arma todo. Para cambiar a EMA basta con devolver `new MediaMovilExponencial(5)` en esa clase.
7. `MercadoController` expone la API y la página en `static/` la consulta para mostrar los paneles.

## Plan de desarrollo

- [x] Definir el modelo de dominio.
- [x] Diagrama de clases UML.
- [x] Implementar objetos de valor y entidades con validaciones.
- [x] Implementar la simulación y el registro de cotizaciones.
- [x] Incorporar Strategy para el cálculo de SMA y EMA.
- [x] Implementar reglas de alertas y estados del seguimiento.
- [x] Crear repositorios y persistencia con JPA.
- [x] Implementar notificaciones mediante interfaces.
- [x] Exponer consultas mediante API REST.
- [x] Crear los paneles de mercado y alertas.
- [x] Pruebas unitarias y documentación técnica.

## Alcance

Este repositorio es una **simulación académica**. No contempla integración obligatoria con exchanges reales, compra o venta de activos, cuentas de inversión, microservicios, mensajería distribuida ni despliegue en producción.

---

**Prioridad del proyecto:** calidad del modelo orientado a objetos, cumplimiento de SOLID, uso justificado de patrones de diseño y pruebas del comportamiento del sistema.
