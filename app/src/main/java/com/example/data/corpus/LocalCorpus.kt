package com.example.data.corpus

import com.example.model.WordEntry

object LocalCorpus {
    val WORDS: List<WordEntry> = listOf(
        // ======================= CIENCIAS =======================
        WordEntry(
            id = "fotosintesis",
            word = "Fotosíntesis",
            phonetic = "/fo.toˈsin.te.sis/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español",
            etymology = "Del griego antiguo φῶς (phôs, 'luz') y σύνθεσις (synthesis, 'composición'). Acuñado en el siglo XIX por botánicos.",
            definitions = listOf(
                "Proceso metabólico biológico por el cual las plantas verdes, algas y ciertas bacterias transforman la energía lumínica solar en energía química, sintetizando glucosa a partir de dióxido de carbono y agua.",
                "Fisiología vegetal: Conjunto de reacciones lumínicas y el ciclo de Calvin que sostiene la mayor parte de la biomasa de la biosfera terrestre produciendo oxígeno molecular como subproducto vital."
            ),
            examples = listOf(
                "En el laboratorio de ciencias de Fe y Alegría 31, los estudiantes observaron la liberación de burbujas de oxígeno durante la fotosíntesis de la elodea.",
                "La preservación de los bosques tropicales es esencial para mantener activa la fotosíntesis a escala planetaria."
            ),
            synonyms = listOf("asimilación clorofílica", "síntesis lumínica"),
            antonyms = listOf("respiración celular", "catabolismo"),
            translations = mapOf(
                "en" to "Photosynthesis",
                "fr" to "Photosynthèse",
                "la" to "Photosynthesis",
                "qu" to "Lliphiy llamk'ay"
            ),
            category = "Ciencias",
            inspirationQuote = "La luz solar que acaricia una hoja se convierte en la savia nutricia del mundo vegetal."
        ),
        WordEntry(
            id = "entropia",
            word = "Entropía",
            phonetic = "/en.tɾoˈpi.a/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español",
            etymology = "Acuñado en 1865 por Rudolf Clausius a partir del griego ἐν (en, 'dentro') y τροπή (tropē, 'transformación').",
            definitions = listOf(
                "Física termodinámica: Magnitud que mide la fracción de energía de un sistema termodinámico que no puede utilizarse para realizar trabajo mecánico.",
                "Teoría de la información: Medida de la incertidumbre o desorden probabilístico inherente a un conjunto de mensajes o datos.",
                "Sentido figurado: Tendencia natural al desorden o degradación estructural en sistemas cerrados o sociales."
            ),
            examples = listOf(
                "La segunda ley de la termodinámica postula que la entropía global del universo tiende inexorablemente a incrementarse con el tiempo.",
                "Un aula organizada y metódica es un esfuerzo constante contra la entropía del caos."
            ),
            synonyms = listOf("desorden térmico", "dispersión energética", "caos sistémico"),
            antonyms = listOf("neguentropía", "orden", "sintropía"),
            translations = mapOf(
                "en" to "Entropy",
                "fr" to "Entropie",
                "la" to "Entropia",
                "qu" to "Chaqway ruway"
            ),
            category = "Ciencias",
            inspirationQuote = "El orden exige energía, constancia y sabiduría para prevalecer frente al desorden."
        ),
        WordEntry(
            id = "epigenetica",
            word = "Epigenética",
            phonetic = "/e.pi.xeˈne.ti.ka/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español",
            etymology = "Del prefijo griego ἐπί (epi, 'sobre' o 'además de') y genética. Término introducido por Conrad Waddington en 1942.",
            definitions = listOf(
                "Estudio de las modificaciones heredables en la expresión génica que no implican cambios directos en la secuencia de nucleótidos del ADN.",
                "Mecanismos moleculares como la metilación del ADN y la modificación de histonas regulados por estímulos ambientales y nutricionales."
            ),
            examples = listOf(
                "La epigenética demuestra que un entorno escolar afectuoso y estimulante impacta positivamente en el desarrollo biológico y cognitivo de los jóvenes."
            ),
            synonyms = listOf("regulación genética", "modificación genómica"),
            antonyms = listOf("determinismo genético"),
            translations = mapOf(
                "en" to "Epigenetics",
                "fr" to "Épigénétique",
                "la" to "Epigenetica",
                "qu" to "Kawsay unanchaq"
            ),
            category = "Ciencias",
            inspirationQuote = "Nuestros genes son la partitura, pero nuestra conducta y el medio ambiente tocan la melodía."
        ),

        // ======================= LITERATURA =======================
        WordEntry(
            id = "metafora",
            word = "Metáfora",
            phonetic = "/meˈta.fo.ɾa/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español",
            etymology = "Del latín metaphŏra, y este del griego μεταφορά (metaphorá, 'traslación', 'traslado de sentido').",
            definitions = listOf(
                "Figura retórica de pensamiento por medio de la cual una realidad o concepto se expresan por medio de una realidad o concepto diferentes con los que guarda cierta semejanza.",
                "Tropo literario que traslada el sentido recto de las voces a otro figurado, enriqueciendo la belleza estética y profundidad del discurso."
            ),
            examples = listOf(
                "El vate César Vallejo usó la metáfora como un puente lírico entre el dolor humano y la solidaridad fraterna.",
                "Decir 'el río es el tiempo que fluye' es una de las metáforas más antiguas de la literatura universal."
            ),
            synonyms = listOf("traslación", "alegoría", "tropo", "símil implícito"),
            antonyms = listOf("literalidad", "sentido estricto"),
            translations = mapOf(
                "en" to "Metaphor",
                "fr" to "Métaphore",
                "la" to "Metaphora",
                "qu" to "Tinkuchina ruranakuy"
            ),
            category = "Literatura",
            inspirationQuote = "La metáfora es quizás una de las potencialidades más fértiles del espíritu humano."
        ),
        WordEntry(
            id = "oximoron",
            word = "Oxímoron",
            phonetic = "/oˈksi.mo.ɾon/",
            partOfSpeech = "Sustantivo masculino",
            language = "es",
            languageName = "Español",
            etymology = "Del griego tardío ὀξύμωρον (oxýmōron), compuesto por ὀξύς (oxýs, 'agudo') y μωρός (mōrós, 'estúpido', 'necio').",
            definitions = listOf(
                "Figura retórica que consiste en complementar una palabra con otra que tiene un significado contradictorio u opuesto.",
                "Combinación sintagmática de dos conceptos de significado antagónico en una sola expresión enriquecedora y paradójica."
            ),
            examples = listOf(
                "La célebre frase poética 'fuego helado' o 'silencio atronador' son ejemplos paradigmáticos de oxímoron.",
                "San Juan de la Cruz describió la experiencia mística con el oxímoron 'música callada, soledad sonora'."
            ),
            synonyms = listOf("paradoja retórica", "contradicción figurada"),
            antonyms = listOf("pleonasmo", "tautología"),
            translations = mapOf(
                "en" to "Oxymoron",
                "fr" to "Oxymore",
                "la" to "Oxymorum",
                "qu" to "Churanakuy simi"
            ),
            category = "Literatura",
            inspirationQuote = "En la aparente contradicción de dos términos opuestos florece una verdad más honda."
        ),
        WordEntry(
            id = "catarsis",
            word = "Catarsis",
            phonetic = "/kaˈtaɾ.sis/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español",
            etymology = "Del griego antiguo κάθαρσις (kátharsis, 'purificación', 'limpieza ceremonial o médica').",
            definitions = listOf(
                "Efecto purificador y liberador que causa la tragedia clásica en los espectadores al suscitar compasión y temor (término aristotélico).",
                "Purificación o liberación emocional que experimenta una persona al verbalizar o expresar vivencias traumáticas reprimidas."
            ),
            examples = listOf(
                "La lectura comunitaria de las novelas de Arguedas produjo una auténtica catarsis de identidad cultural entre los estudiantes.",
                "Escribir poemas en su diario escolar fue para ella un ejercicio de catarsis íntima."
            ),
            synonyms = listOf("purificación", "liberación", "desahogo", "redención"),
            antonyms = listOf("represión", "bloqueo", "inhibición"),
            translations = mapOf(
                "en" to "Catharsis",
                "fr" to "Catharsis",
                "la" to "Catharsis",
                "qu" to "Chuqyachi llakisqa"
            ),
            category = "Literatura",
            inspirationQuote = "A través del arte y la poesía, el alma humana depura sus dolores más callados."
        ),

        // ======================= FILOSOFÍA =======================
        WordEntry(
            id = "epistemologia",
            word = "Epistemología",
            phonetic = "/e.pis.te.mo.loˈxi.a/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español",
            etymology = "Del griego ἐπιστήμη (epistēmē, 'conocimiento justificado', 'ciencia') y -λογία (-logía, 'estudio', 'tratado').",
            definitions = listOf(
                "Rama de la filosofía que estudia los fundamentos, principios, límites, validez y métodos del conocimiento humano.",
                "Teoría del conocimiento científico que analiza cómo se contrastan, justifican y validan las hipótesis científicas."
            ),
            examples = listOf(
                "La epistemología contemporánea cuestiona las certezas absolutas y propone un saber abierto a la contrastación rigurosa.",
                "En el curso de Filosofía de Fe y Alegría 31, se debatió sobre la epistemología constructivista de Jean Piaget."
            ),
            synonyms = listOf("gnoseología", "teoría del conocimiento", "filosofía de la ciencia"),
            antonyms = listOf("dogmatismo", "oscurantismo"),
            translations = mapOf(
                "en" to "Epistemology",
                "fr" to "Épistémologie",
                "la" to "Epistemologia",
                "qu" to "Yachay yachay"
            ),
            category = "Filosofía",
            inspirationQuote = "Preguntarse cómo sabemos lo que creemos saber es el comienzo de la verdadera libertad intelectual."
        ),
        WordEntry(
            id = "mayeutica",
            word = "Mayéutica",
            phonetic = "/maˈxeu.ti.ka/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español",
            etymology = "Del griego μαιευτική (maieutikē, 'arte de las comadronas o parteras'). Método atribuido a Sócrates.",
            definitions = listOf(
                "Método socrático con que el maestro, mediante preguntas sistemáticas y dialogadas, hace que el discípulo descubra por sí mismo las nociones que ya yacen latentes en su espíritu.",
                "Pedagogía reflexiva basada en el alumbramiento dialógico del conocimiento interior frente a la simple memorización pasiva."
            ),
            examples = listOf(
                "Nuestros educadores emplean la mayéutica para que los alumnos formulen preguntas críticas sobre su entorno social.",
                "Sócrates comparaba su labor filosófica con el oficio de su madre Fenáreta: dar a luz no cuerpos, sino almas y verdades."
            ),
            synonyms = listOf("método socrático", "diálogo heurístico", "pedagogía dialógica"),
            antonyms = listOf("adoctrinamiento", "dogmatismo"),
            translations = mapOf(
                "en" to "Maieutics",
                "fr" to "Maïeutique",
                "la" to "Maieutica",
                "qu" to "Yuyay paqarichiq"
            ),
            category = "Filosofía",
            inspirationQuote = "Educar no es llenar una vasija vacía, sino encender una antorcha que ya posee combustible propio."
        ),
        WordEntry(
            id = "dialéctica",
            word = "Dialéctica",
            phonetic = "/djaˈlek.ti.ka/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español",
            etymology = "Del latín dialectĭca, y este del griego διαλεκτική (dialektikē, 'técnica de la conversación').",
            definitions = listOf(
                "Filosofía: Arte de dialogar, argumentar y discernir la verdad a través del contraste recíproco de opiniones y razones contrapuestas.",
                "Teoría ontológica de Hegel y Marx que concibe la realidad y el devenir histórico como el movimiento generado por la contradicción (tesis, antítesis y síntesis)."
            ),
            examples = listOf(
                "El debate estudiantil fomentó una rica dialéctica donde la síntesis superó los desacuerdos iniciales.",
                "La dialéctica enseña que el cambio y la transformación son constantes universales en la sociedad."
            ),
            synonyms = listOf("debate argumentativo", "dinámica de contradicciones", "lógica dialógica"),
            antonyms = listOf("inmovilismo", "unilateralidad"),
            translations = mapOf(
                "en" to "Dialectics",
                "fr" to "Dialectique",
                "la" to "Dialectica",
                "qu" to "Rimay rimanakuy"
            ),
            category = "Filosofía",
            inspirationQuote = "Del choque fecundo de tesis encontradas brota la chispa iluminadora de una verdad superior."
        ),

        // ======================= PERUANISMOS =======================
        WordEntry(
            id = "yapa",
            word = "Yapa",
            phonetic = "/ˈʝa.pa/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español (Peruanismo)",
            etymology = "Del quechua yapa ('aumento', 'añadidura'). Incorporado plenamente al habla popular peruana y andina.",
            definitions = listOf(
                "Perú, Bolivia, Ecuador y Chile: Pequeña añadidura gratuita que el vendedor entrega al comprador por cortesía o fidelidad tras una transacción comercial.",
                "Sentido amplio: Regalo imprevisto, propina o gracia adicional a lo estrictamente pactado."
            ),
            examples = listOf(
                "En el mercado del barrio, la casera siempre me regala una yapa generosa de culantro y ají amarillo.",
                "El profesor dio cinco minutos de yapa para que todos pudieran culminar el ensayo con tranquilidad."
            ),
            synonyms = listOf("añadidura", "ñapa", "gracia", "aumento", "cortesía"),
            antonyms = listOf("recorte", "merma", "descuento"),
            translations = mapOf(
                "en" to "Bonus / free extra",
                "fr" to "Petit supplément gratuit",
                "la" to "Auctarium",
                "qu" to "Yapa"
            ),
            category = "Peruanismos",
            inspirationQuote = "La yapa no es solo un obsequio; es un símbolo entrañable de reciprocidad, cariño y convivencia comunitaria."
        ),
        WordEntry(
            id = "choclo",
            word = "Choclo",
            phonetic = "/ˈt͡ʃo.klo/",
            partOfSpeech = "Sustantivo masculino",
            language = "es",
            languageName = "Español (Peruanismo)",
            etymology = "Del quechua chuqllu ('mazorca tierna de maíz').",
            definitions = listOf(
                "Mazorca tierna de maíz, especialmente la variedad peruana de grano gigante del Valle Sagrado de los Incas (Urubamba), que se consume cocida con queso fresco.",
                "Alimento emblemático y ancestral de la gastronomía andina del Perú."
            ),
            examples = listOf(
                "En la feria gastronómica escolar, el plato estrella fue choclo sancochado con queso andino y ají huacatay.",
                "Los granos lechosos y tiernos del choclo cusqueño son reconocidos en el mundo entero por su dulzura única."
            ),
            synonyms = listOf("mazorca tierna", "elote", "choclo cusqueño"),
            antonyms = listOf(),
            translations = mapOf(
                "en" to "Fresh corn cob (Andean giant corn)",
                "fr" to "Épi de maïs frais géant",
                "la" to "Spica zeae",
                "qu" to "Chuqllu"
            ),
            category = "Peruanismos",
            inspirationQuote = "El choclo es la ternura del maíz andino que nutre el cuerpo y conecta nuestras raíces."
        ),
        WordEntry(
            id = "calato",
            word = "Calato",
            phonetic = "/kaˈla.to/",
            partOfSpeech = "Adjetivo / Sustantivo",
            language = "es",
            languageName = "Español (Peruanismo)",
            etymology = "Del quechua q'ala ('desnudo', 'pelado', 'descubierto'). Muy extendido en todo el Perú.",
            definitions = listOf(
                "Desnudo, desprovisto de ropas o vestiduras.",
                "Coloquial: Desabrigado, vulnerable al frío o a la intemperie.",
                "Económico (coloquial): Sin dinero o recursos monetarios en un momento dado."
            ),
            examples = listOf(
                "El bebé salió gateando calato después del baño ante la risa de toda la familia.",
                "¡Abrígate bien que no puedes salir tan calato con este invierno limeño!"
            ),
            synonyms = listOf("desnudo", "desabrigado", "descubierto", "en cueros"),
            antonyms = listOf("vestido", "arropado", "abrigado"),
            translations = mapOf(
                "en" to "Naked / stripped of clothes",
                "fr" to "Nu / dévêtu",
                "la" to "Nudus",
                "qu" to "Q'ala"
            ),
            category = "Peruanismos",
            inspirationQuote = "La palabra q'ala nos recuerda la sencillez natural y despojada con la que venimos al mundo."
        ),
        WordEntry(
            id = "huarique",
            word = "Huarique",
            phonetic = "/waˈɾi.ke/",
            partOfSpeech = "Sustantivo masculino",
            language = "es",
            languageName = "Español (Peruanismo)",
            etymology = "Del quechua wari ('escondido', 'oculto') y quizás del aimara o sufijo hispano. Lugar tradicional secreto.",
            definitions = listOf(
                "Local gastronómico popular y modesto, a menudo poco publicitado o escondido, conocido por preparar comidas exquisitas, caseras y abundantes.",
                "Lugar reservado o rincón íntimo donde un grupo de amigos se congrega para departir y comer bien."
            ),
            examples = listOf(
                "En San Juan de Lurigancho hay un huarique extraordinario donde preparan el mejor cebiche de barrio.",
                "Los conocedores prefieren la autenticidad de un huarique tradicional antes que un restaurante pretencioso."
            ),
            synonyms = listOf("picantina", "caleta gastronómica", "rincón secreto"),
            antonyms = listOf("restaurante de cadena"),
            translations = mapOf(
                "en" to "Hidden culinary gem / local food haven",
                "fr" to "Petit resto secret et authentique",
                "la" to "Popina recondita",
                "qu" to "Wari mikhuna wasi"
            ),
            category = "Peruanismos",
            inspirationQuote = "En los huariques late el corazón generoso y sazonado del pueblo trabajador."
        ),

        // ======================= VOCABULARIO ACADÉMICO =======================
        WordEntry(
            id = "paradigma",
            word = "Paradigma",
            phonetic = "/pa.ɾaˈdiɣ.ma/",
            partOfSpeech = "Sustantivo masculino",
            language = "es",
            languageName = "Español",
            etymology = "Del latín tardío paradigma, y este del griego παράδειγμα (parádeigma, 'modelo', 'ejemplo demostrativo').",
            definitions = listOf(
                "Teoría o conjunto de teorías cuyo núcleo central se acepta sin cuestionar y que suministra la base y modelo para resolver problemas en una comunidad científica (Thomas Kuhn).",
                "Ejemplo o ejemplar de gran perfección que se propone como norma o patrón a seguir en cualquier disciplina."
            ),
            examples = listOf(
                "La propuesta pedagógica de Fe y Alegría constituye un paradigma de educación popular transformadora y solidaria.",
                "La física cuántica provocó un cambio de paradigma radical respecto a la mecánica clásica newtoniana."
            ),
            synonyms = listOf("modelo", "patrón", "arquetipo", "marco conceptual", "ejemplo"),
            antonyms = listOf("anomalía", "excepción"),
            translations = mapOf(
                "en" to "Paradigm",
                "fr" to "Paradigme",
                "la" to "Paradigma",
                "qu" to "Qhawaq yachay"
            ),
            category = "Vocabulario Académico",
            inspirationQuote = "Romper un viejo paradigma requiere valentía; construir uno nuevo exige rigor y amor al prójimo."
        ),
        WordEntry(
            id = "resiliencia",
            word = "Resiliencia",
            phonetic = "/re.siˈljen.sja/",
            partOfSpeech = "Sustantivo femenino",
            language = "es",
            languageName = "Español",
            etymology = "Del latín resiliens, resilīre ('saltar hacia atrás', 'rebotar', 'recuperar la forma original').",
            definitions = listOf(
                "Psicología: Capacidad humana de adaptarse positivamente a situaciones adversas, traumas o estrés severo, saliendo fortalecido.",
                "Física e ingeniería: Propiedad de ciertos materiales para recuperar su forma inicial tras haber sido sometidos a presiones deformadoras."
            ),
            examples = listOf(
                "La comunidad educativa de la I.E. Fe y Alegría 31 demostró una admirable resiliencia ante cualquier desafío social.",
                "La resiliencia no es la ausencia de dolor, sino la decisión valerosa de sobreponerse con esperanza."
            ),
            synonyms = listOf("fortaleza", "tenacidad", "perseverancia", "entereza", "recuperación"),
            antonyms = listOf("fragilidad", "vulnerabilidad", "rendición"),
            translations = mapOf(
                "en" to "Resilience",
                "fr" to "Résilience",
                "la" to "Resilientia",
                "qu" to "Sinchi kay"
            ),
            category = "Vocabulario Académico",
            inspirationQuote = "La resiliencia convierte las piedras del camino en los peldaños de nuestra superación personal.",
            isWordOfDay = true
        ),
        WordEntry(
            id = "elucidar",
            word = "Elucidar",
            phonetic = "/e.lu.siˈdaɾ/",
            partOfSpeech = "Verbo transitivo",
            language = "es",
            languageName = "Español",
            etymology = "Del latín elucidāre, compuesto por e- (intensivo) y lucidus ('claro', 'luminoso').",
            definitions = listOf(
                "Poner en claro, explicar, esclarecer exhaustivamente un punto dudoso, un enigma o una materia compleja.",
                "Hacer inteligible una teoría mediante argumentos demostrativos y evidencia tangible."
            ),
            examples = listOf(
                "El objetivo de la tesis monográfica es elucidar las causas estructurales de la deserción escolar temprana.",
                "Con gran maestría pedagógica, la profesora elucidó el difícil problema de geometría analítica."
            ),
            synonyms = listOf("esclarecer", "desentrañar", "dilucidar", "clarificar", "iluminar"),
            antonyms = listOf("confundir", "obscurecer", "enredar"),
            translations = mapOf(
                "en" to "Elucidate",
                "fr" to "Élucider",
                "la" to "Elucidare",
                "qu" to "Sut'inchay"
            ),
            category = "Vocabulario Académico",
            inspirationQuote = "El conocimiento genuino no busca deslumbrar, sino elucidar las tinieblas de la ignorancia."
        ),
        WordEntry(
            id = "axioma",
            word = "Axioma",
            phonetic = "/aɡˈsjo.ma/",
            partOfSpeech = "Sustantivo masculino",
            language = "es",
            languageName = "Español",
            etymology = "Del latín axiōma, y este del griego ἀξίωμα (axíōma, 'lo que parece justo', 'proposición evidente', 'digno').",
            definitions = listOf(
                "Proposición tan clara y evidente que se admite sin demostración previa y sirve de fundamento a una ciencia matemática o lógica.",
                "Sentencia admitida comúnmente como principio rector indiscutible."
            ),
            examples = listOf(
                "Que 'el todo es mayor que cualquiera de sus partes' fue formulado por Euclides como un axioma fundamental.",
                "Para la educación popular, el derecho inalienable de todo niño a aprender es un axioma ético supremo."
            ),
            synonyms = listOf("postulado", "principio elemental", "premisa evidente"),
            antonyms = listOf("hipótesis no probada", "duda", "falacia"),
            translations = mapOf(
                "en" to "Axiom",
                "fr" to "Axiome",
                "la" to "Axioma",
                "qu" to "Chiqaq takyachisqa"
            ),
            category = "Vocabulario Académico",
            inspirationQuote = "La dignidad humana es el axioma innegociable de toda sociedad justa."
        ),

        // ======================= MULTILINGÜE: INGLÉS =======================
        WordEntry(
            id = "serendipity",
            word = "Serendipity",
            phonetic = "/ˌsɛr.ənˈdɪp.ɪ.ti/",
            partOfSpeech = "Noun (Sustantivo)",
            language = "en",
            languageName = "Inglés",
            etymology = "Acuñado en 1754 por Horace Walpole aludiendo al cuento persa 'Los tres príncipes de Serendip' (Sri Lanka).",
            definitions = listOf(
                "The occurrence and development of events by chance in a happy, fortunate or beneficial way.",
                "Un hallazgo afortunado, valioso e inesperado que se produce de manera casual mientras se busca otra cosa."
            ),
            examples = listOf(
                "Alexander Fleming's discovery of penicillin was a monumental triumph of serendipity in medical science.",
                "Finding my favorite book at a small street corner stall was pure serendipity."
            ),
            synonyms = listOf("happy coincidence", "fluke", "fortune", "fortuity", "chiripa"),
            antonyms = listOf("misfortune", "adversity", "design"),
            translations = mapOf(
                "es" to "Serendipia",
                "fr" to "Sérendipité",
                "la" to "Fortuna felix",
                "qu" to "Kusikuy tariy"
            ),
            category = "Ciencias",
            inspirationQuote = "La suerte favorece a las mentes preparadas para advertir lo imprevisto."
        ),
        WordEntry(
            id = "ephemeral",
            word = "Ephemeral",
            phonetic = "/ɪˈfɛm.ər.əl/",
            partOfSpeech = "Adjective (Adjetivo)",
            language = "en",
            languageName = "Inglés",
            etymology = "From Greek ἐφήμερος (ephēmeros), from ἐπί (epi, 'on') and ἡμέρα (hēmera, 'day'): lasting only one day.",
            definitions = listOf(
                "Lasting for a very short, transient period of time; fleeting.",
                "Que dura un solo día o que es de muy corta duración, fugaz y pasajero."
            ),
            examples = listOf(
                "The morning dew upon the school gardens proved to be ephemeral under the golden morning sunshine.",
                "Human fame is often ephemeral, but moral deeds endure forever."
            ),
            synonyms = listOf("transient", "fleeting", "evanescent", "fugaz", "efímero"),
            antonyms = listOf("eternal", "perpetual", "permanent"),
            translations = mapOf(
                "es" to "Efímero",
                "fr" to "Éphémère",
                "la" to "Ephemerus",
                "qu" to "Pisi pachallapaq"
            ),
            category = "Literatura",
            inspirationQuote = "Lo efímero de las flores hace aún más preciosa cada gota de su fragancia."
        ),
        WordEntry(
            id = "ubiquitous",
            word = "Ubiquitous",
            phonetic = "/juːˈbɪk.wɪ.təs/",
            partOfSpeech = "Adjective (Adjetivo)",
            language = "en",
            languageName = "Inglés",
            etymology = "From Latin ubīque ('everywhere'), from ubi ('where') + -que ('any, ever').",
            definitions = listOf(
                "Present, appearing, or found everywhere at once; omnipresent.",
                "Que está presente a un mismo tiempo en todas partes; omnipresente."
            ),
            examples = listOf(
                "In our modern digital era, smart computing devices have become nearly ubiquitous.",
                "The warmth of community solidarity is ubiquitous in our educational institution."
            ),
            synonyms = listOf("omnipresent", "pervasive", "universal"),
            antonyms = listOf("rare", "scarce", "localized"),
            translations = mapOf(
                "es" to "Ubícuo / Omnipresente",
                "fr" to "Omniprésent",
                "la" to "Ubiquitarius",
                "qu" to "Maypipas kashaq"
            ),
            category = "Vocabulario Académico",
            inspirationQuote = "El amor y la bondad pueden y deben ser ubicuos en la escuela."
        ),

        // ======================= MULTILINGÜE: FRANCÉS =======================
        WordEntry(
            id = "renaissance",
            word = "Renaissance",
            phonetic = "/ʁə.nɛ.sɑ̃s/",
            partOfSpeech = "Nom féminin (Sustantivo femenino)",
            language = "fr",
            languageName = "Francés",
            etymology = "Formé sur le verbe renaître (du latin renasci, 'naître à nouveau').",
            definitions = listOf(
                "Période historique et mouvement culturel européen (XIVe-XVIe siècles) marquant un renouveau des lettres, des arts et de la philosophie.",
                "Nouvelle naissance, réveil ou restauration d'une société, d'une culture ou d'un idéal spirituel."
            ),
            examples = listOf(
                "Chaque nouveau projet éducatif représente une véritable renaissance intellectuelle pour nos élèves.",
                "La Renaissance a placé l'être humain et sa créativité au centre de l'univers."
            ),
            synonyms = listOf("renouveau", "résurrection", "renouveau culturel", "renacimiento"),
            antonyms = listOf("décadence", "déclin", "mort"),
            translations = mapOf(
                "es" to "Renacimiento",
                "en" to "Renaissance / Rebirth",
                "la" to "Renascientia",
                "qu" to "Kutipaq kawsay"
            ),
            category = "Literatura",
            inspirationQuote = "Cada amanecer encierra la promesa de un renacimiento interior."
        ),
        WordEntry(
            id = "flaneur",
            word = "Flâneur",
            phonetic = "/flɑ.nœʁ/",
            partOfSpeech = "Nom masculin (Sustantivo masculino)",
            language = "fr",
            languageName = "Francés",
            etymology = "Issu du verbe flâner (d'origine scandinave / normande flana, 'courir çà et là'). Popularisé par Charles Baudelaire.",
            definitions = listOf(
                "Personne qui se promène sans hâte, observant avec curiosité poétique la vie urbaine et les gens.",
                "Figure littéraire du spectateur passionné qui goûte la foule et l'architecture citadine."
            ),
            examples = listOf(
                "Baudelaire a immortalisé le flâneur comme l'âme attentive des métropoles modernes.",
                "Il aimait être un flâneur dans les rues historiques de Lima, contemplant les balcons coloniaux."
            ),
            synonyms = listOf("promeneur", "observateur", "badaud", "paseante"),
            antonyms = listOf("pressé", "homme affairé"),
            translations = mapOf(
                "es" to "Flâneur / Paseante observador",
                "en" to "Stroller / Idler / Flâneur",
                "la" to "Spatiator",
                "qu" to "Qhawaq puriq"
            ),
            category = "Filosofía",
            inspirationQuote = "Pasear despacio con la mirada despierta es una forma sutil de poesía."
        ),

        // ======================= MULTILINGÜE: LATÍN =======================
        WordEntry(
            id = "carpe_diem",
            word = "Carpe diem",
            phonetic = "/ˈkar.pe ˈdi.em/",
            partOfSpeech = "Locución latina",
            language = "la",
            languageName = "Latín",
            etymology = "Sentencia del poeta romano Horacio (Odas, I, 11, 8): 'Carpe diem, quam minimum credula postero' ('Aprovecha el día de hoy, no confíes en el mañana').",
            definitions = listOf(
                "Tópico literario y filosófico que exhorta a gozar del momento presente sin dejar escapar el tiempo fugitivo.",
                "Actitud vital sabia que valora la intensidad ética, afectiva y cognoscitiva del presente."
            ),
            examples = listOf(
                "La sentencia 'Carpe diem' nos invita a no postergar nuestros sueños y buenas acciones para un futuro incierto.",
                "Horacio concebía el carpe diem no como desenfreno, sino como la sabia cosecha del instante presente."
            ),
            synonyms = listOf("vive el presente", "aprovecha el día", "tempus fugit"),
            antonyms = listOf("procrastinación", "desidia"),
            translations = mapOf(
                "es" to "Aprovecha el presente",
                "en" to "Seize the day",
                "fr" to "Cueille le jour présent",
                "qu" to "Kunan punchawta hap'iy"
            ),
            category = "Filosofía",
            inspirationQuote = "El presente es el único tiempo donde podemos actuar, amar y transformar la historia."
        ),
        WordEntry(
            id = "alma_mater",
            word = "Alma mater",
            phonetic = "/ˈal.ma ˈma.ter/",
            partOfSpeech = "Locución sustantiva latina",
            language = "la",
            languageName = "Latín",
            etymology = "Del latín almus ('nutricio', 'propicio') y mater ('madre'). Lema de la Universidad de Bolonia (1088).",
            definitions = listOf(
                "Metáfora para designar a la institución educativa (colegio, universidad o escuela) donde una persona se ha formado intelectualmente.",
                "Madre nutricia que alimenta el espíritu de los alumnos con los frutos de la ciencia y los valores morales."
            ),
            examples = listOf(
                "La I.E. Fe y Alegría 31 es la entrañable alma mater de generaciones de jóvenes comprometidos con el Perú.",
                "Con orgullo y gratitud, los exalumnos regresaron a celebrar el aniversario de su alma mater."
            ),
            synonyms = listOf("madre nutricia", "casa de estudios", "escuela nutricia"),
            antonyms = listOf(),
            translations = mapOf(
                "es" to "Alma máter / Casa de estudios",
                "en" to "Alma mater / Fostering mother",
                "fr" to "Alma mater / École nourricière",
                "qu" to "Yachay mama wasi"
            ),
            category = "Vocabulario Académico",
            inspirationQuote = "Una escuela viva es un hogar intelectual que nutre el alma para toda la existencia."
        ),
        WordEntry(
            id = "cogito_ergo_sum",
            word = "Cogito ergo sum",
            phonetic = "/ˈkoː.ɡi.toː ˈer.ɡoː sum/",
            partOfSpeech = "Proposición filosófica latina",
            language = "la",
            languageName = "Latín",
            etymology = "Formulada por René Descartes en el Discurso del Método (1637) en francés ('Je pense, donc je suis') y vertida al latín en sus Principia Philosophiae (1644).",
            definitions = listOf(
                "Principio primero e indubitable de la filosofía cartesiana: al dudar de todo, constato con certeza mi propio acto de pensar y, por ende, mi propia existencia.",
                "Piedra angular del racionalismo moderno que sitúa la autoconciencia subjetiva como fundamento del conocimiento certero."
            ),
            examples = listOf(
                "El célebre 'Cogito ergo sum' inauguró la modernidad filosófica al fundar el saber en la certeza del pensamiento reflexivo."
            ),
            synonyms = listOf("pienso, luego existo", "certeza subjetiva", "autoconciencia"),
            antonyms = listOf("escepticismo radical"),
            translations = mapOf(
                "es" to "Pienso, luego existo",
                "en" to "I think, therefore I am",
                "fr" to "Je pense, donc je suis",
                "qu" to "Yuyaymanani, chayraykun kani"
            ),
            category = "Filosofía",
            inspirationQuote = "Dudar es pensar, y pensar es la prueba irrefutable de que somos seres conscientes y libres."
        ),

        // ======================= MULTILINGÜE: QUECHUA =======================
        WordEntry(
            id = "sumaq_kawsay",
            word = "Sumaq Kawsay",
            phonetic = "/suˈmaχ kawˈsaj/",
            partOfSpeech = "Locución quechua / Principio andino",
            language = "qu",
            languageName = "Quechua",
            etymology = "Del quechua sumaq ('hermoso', 'bueno', 'pleno') y kawsay ('vida', 'existencia'). Sabiduría ancestral andina.",
            definitions = listOf(
                "Buen Vivir / Vida en plenitud: Filosofía ancestral andina que propone la vida armónica entre los seres humanos, la comunidad y la Madre Tierra (Pachamama).",
                "Paradigma ético y ecológico de desarrollo que prioriza la solidaridad, la reciprocidad y el equilibrio ambiental por encima del lucro desmedido."
            ),
            examples = listOf(
                "El Sumaq Kawsay nos enseña que el verdadero progreso respeta los ciclos de la naturaleza y dignifica a las personas.",
                "En las aulas de Fe y Alegría 31 aprendemos el valor del Sumaq Kawsay a través del trabajo cooperativo y el cuidado del biohuerto."
            ),
            synonyms = listOf("Buen Vivir", "vida armónica", "plenitud comunitaria"),
            antonyms = listOf("depredación", "individualismo", "desarmonía"),
            translations = mapOf(
                "es" to "Buen Vivir / Vida en Plenitud",
                "en" to "Good Living / Life in Fullness",
                "fr" to "Bien Vivre / Vie harmonieuse",
                "la" to "Vita beata et concors"
            ),
            category = "Filosofía",
            inspirationQuote = "Vivir en plenitud es caminar al unísono con el latido sagrado de la tierra que nos cobija."
        ),
        WordEntry(
            id = "ayni",
            word = "Ayni",
            phonetic = "/ˈaj.ni/",
            partOfSpeech = "Sustantivo quechua",
            language = "qu",
            languageName = "Quechua",
            etymology = "Voz quechua milenaria que expresa el principio de correspondencia y mutualidad andina.",
            definitions = listOf(
                "Principio de ayuda mutua y reciprocidad comunitaria: 'Hoy por ti, mañana por mí'. Colaboración solidaria entre familias para la siembra, cosecha o construcción.",
                "Ley cósmica y social andina según la cual toda energía, favor o don brindado es devuelto en un ciclo virtuoso de armonía comunal."
            ),
            examples = listOf(
                "Cuando construimos los murales del colegio, practicamos un verdadero ayni de manos y corazones generosos.",
                "El ayni demostró la fuerza invencible de los pueblos andinos para superar hambrunas y dificultades."
            ),
            synonyms = listOf("reciprocidad", "ayuda mutua", "solidaridad comunal", "cooperación"),
            antonyms = listOf("egoísmo", "usura", "individualismo"),
            translations = mapOf(
                "es" to "Reciprocidad / Ayuda mutua",
                "en" to "Mutual reciprocity / Sacred exchange",
                "fr" to "Réciprocité solidaire",
                "la" to "Mutuum auxilium"
            ),
            category = "Peruanismos",
            inspirationQuote = "Hoy por ti, mañana por mí: el corazón que da con alegría recibe multiplicado en bendición."
        ),
        WordEntry(
            id = "pachamama",
            word = "Pachamama",
            phonetic = "/pa.t͡ʃaˈma.ma/",
            partOfSpeech = "Sustantivo femenino quechua",
            language = "qu",
            languageName = "Quechua",
            etymology = "Del quechua pacha ('espacio', 'tiempo', 'universo', 'mundo') y mama ('madre').",
            definitions = listOf(
                "Madre Tierra: Divinidad andina protectora de la fertilidad de los campos, las aguas, los animales y la vida en la cosmovisión incaica y contemporánea.",
                "Concepción integral de la biosfera como un ser vivo y sagrado digno de reverencia, cuidado y gratitud (pagapu / tinkuy)."
            ),
            examples = listOf(
                "El 1 de agosto celebramos el día de agradecimiento a la Pachamama con ofrendas florales y respeto ecológico.",
                "Cuidar el agua y no arrojar plásticos es la forma más honesta de amar a la Pachamama."
            ),
            synonyms = listOf("Madre Tierra", "Tierra nutricia", "cosmos vivo"),
            antonyms = listOf(),
            translations = mapOf(
                "es" to "Madre Tierra",
                "en" to "Mother Earth",
                "fr" to "Terre Mère",
                "la" to "Mater Tellus"
            ),
            category = "Ciencias",
            inspirationQuote = "No somos dueños de la tierra; somos sus hijos encargados de custodiar su florecimiento."
        ),
        WordEntry(
            id = "yachay",
            word = "Yachay",
            phonetic = "/ˈja.t͡ʃaj/",
            partOfSpeech = "Verbo / Sustantivo quechua",
            language = "qu",
            languageName = "Quechua",
            etymology = "Raíz quechua vinculada al saber, al aprendizaje y a la transmisión de conocimientos ancestrales y científicos.",
            definitions = listOf(
                "Saber, aprender, conocimiento, ciencia o sabiduría transmitida de generación en generación.",
                "Uno de los tres pilares de la ética andina junto con el Munay (querer con amor) y el Llank'ay (trabajar con empeño)."
            ),
            examples = listOf(
                "El Yachay Wasi era la casa del saber donde los sabios amautas educaban a la juventud en el incanato.",
                "En la I.E. Fe y Alegría 31 cultivamos el Yachay con pasión, rigor científico y sentido de servicio fraterno."
            ),
            synonyms = listOf("sabiduría", "conocimiento", "saber profundo"),
            antonyms = listOf("ignorancia", "desconocimiento"),
            translations = mapOf(
                "es" to "Saber / Conocimiento / Sabiduría",
                "en" to "Knowledge / Wisdom / Learning",
                "fr" to "Savoir / Sagesse",
                "la" to "Sapientia / Scientia"
            ),
            category = "Vocabulario Académico",
            inspirationQuote = "El conocimiento que no se comparte con amor se marchita como una flor sin agua."
        )
    )
}
