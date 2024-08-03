package unsam.phm.grupo1



import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import unsam.phm.grupo1.builder.LodgingBuilder
import unsam.phm.grupo1.builder.UserBuilder
import unsam.phm.grupo1.entity.*
import unsam.phm.grupo1.mongo.LodgingRepository
import unsam.phm.grupo1.neo4j.NeoLodgingRepository
import unsam.phm.grupo1.neo4j.NeoUserRepository
import unsam.phm.grupo1.repository.UserRepository

@Service
@Profile("!test")
class AirphmBootstrap: InitializingBean, DisposableBean {

    // Injections
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var lodgingRepository: LodgingRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var neoUserRepository: NeoUserRepository

    @Autowired
    private lateinit var neoLodgingRepository: NeoLodgingRepository

    @Value("\${spring.jpa.hibernate.ddl-auto}")
    lateinit var dbConfig: String

    // Usuarios
    private lateinit var nicolas: User
    private lateinit var rodrigo: User
    private lateinit var leandro: User
    private lateinit var alex: User
    private lateinit var matias: User
//
//    // Alojamientos
//    // Los alojamientos no hace falta que los guardemos en variables por ahora
//

    fun saveUsers() {
        val defaultPassword = "123456789"

        nicolas = UserBuilder(User(), userRepository, neoUserRepository)
            .withEmail("nicovillamonte@gmail.com")
            .withUsername("nicovillamonte")
            .withPassword(defaultPassword).save()

        rodrigo = UserBuilder(User(), userRepository, neoUserRepository)
            .withEmail("ralonso@estudiantes.unsam.edu.ar")
            .withUsername("ralonso")
            .withFriends(mutableSetOf(nicolas))
            .withPassword(defaultPassword).save()

        leandro = UserBuilder(User(), userRepository, neoUserRepository)
            .withEmail("leandroamarilla50@gmail.com")
            .withUsername("leandroamarilla")
            .withFriends(mutableSetOf(nicolas, rodrigo))
            .withPassword(defaultPassword).save()

        alex = UserBuilder(User(), userRepository, neoUserRepository)
            .withEmail("alex.rza@hotmail.com")
            .withUsername("nicoraza")
            .withFriends(mutableSetOf(leandro))
            .withPassword(defaultPassword).save()

        matias = UserBuilder(User(), userRepository, neoUserRepository)
            .withEmail("mati.ganino@gmail.com")
            .withUsername("matiganino")
            .withFriends(mutableSetOf(nicolas, rodrigo, leandro, alex))
            .withPassword(defaultPassword).save()
    }
//
    fun saveLodgings() {
        //---------------------------------- CASAS ----------------------------------
        LodgingBuilder(House(), lodgingRepository)
            .withMandatoryData(
                name = "Casa de campo",
                description = "Casa de campo en el medio de la nada",
                country = "Argentina",
                address = "Tutan Kamon 123",
                baseCost = 1000.toFloat(),
                capacity = 4,
                rooms = 2,
                baths = 1,
                detailLodging = "Casa de campo en el medio de la nada",
                aspects = "Casa de campo en el medio de la nada",
                owner = nicolas
            )
            .withImage("https://i.ytimg.com/vi/B4xRxRAwbh0/hqdefault.jpg")
            .withHouseKeeping(true)
            .save()

        LodgingBuilder(House(), lodgingRepository)
            .withMandatoryData(
                name = "Casa en la playa",
                description = "Una hermosa casa con vista al mar, ubicada en la costa de Uruguay. Cuenta con amplios espacios y una decoración moderna y acogedora. Perfecta para una escapada de fin de semana con amigos o familiares.",
                country = "Uruguay",
                address = "Av. Del Mar 1234",
                baseCost = 2000.toFloat(),
                capacity = 4,
                rooms = 2,
                baths = 1,
                detailLodging = "La casa cuenta con amplios espacios y una decoración moderna y acogedora. Perfecta para una escapada de fin de semana con amigos o familiares.",
                aspects = "Vista al mar, cocina equipada, aire acondicionado",
                owner = matias
            )
            .withImage("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/casa-playa-surf-edificio-1627031468.jpg")
            .withHouseKeeping(true)
            .save()

        LodgingBuilder(House(), lodgingRepository)
            .withMandatoryData(
                name = "Casa de campo en las sierras",
                description = "Una hermosa casa de campo ubicada en las sierras de Córdoba, Argentina. Rodeada de naturaleza y paisajes increíbles, cuenta con todas las comodidades para una estadía relajante y placentera.",
                country = "Argentina",
                address = "Ruta 40, km 123",
                baseCost = 3000.toFloat(),
                capacity = 6,
                rooms = 3,
                baths = 2,
                detailLodging = "La casa cuenta con todas las comodidades para una estadía relajante y placentera.",
                aspects = "Paisajes increíbles, piscina, parrilla",
                owner = rodrigo
            )
            .withImage("https://q-xx.bstatic.com/xdata/images/hotel/840x460/183538622.jpg?k=af22d2744a1feac122c1f800d34155e2491083a25c325f8655ab38dfbfe51eab&o=")
            .withHouseKeeping(false)
            .save()

        LodgingBuilder(House(), lodgingRepository)
            .withMandatoryData(
                name = "Casa moderna en la ciudad",
                description = "Una casa moderna y elegante ubicada en el centro de Buenos Aires. Cerca de los principales atractivos turísticos de la ciudad, cuenta con todas las comodidades para una estadía confortable y placentera.",
                country = "Argentina",
                address = "Av. Corrientes 1234",
                baseCost = 4000.toFloat(),
                capacity = 4,
                rooms = 2,
                baths = 2,
                detailLodging = "La casa cuenta con todas las comodidades para una estadía confortable y placentera.",
                aspects = "Cerca de atractivos turísticos, terraza, aire acondicionado",
                owner = alex
            )
            .withImage("https://2.bp.blogspot.com/-O5E2n7EzJ9o/Wfdey40XZ4I/AAAAAAAAWOM/psjT5xHLt4o7mJO826tzk9zfGvhlYRk-QCLcBGAs/s640/casa%2Ben%2Bmexico%2B2.jpg")
            .withHouseKeeping(true)
            .save()

        //---------------------------------- DEPARTAMENTOS ----------------------------------

        LodgingBuilder(Building(), lodgingRepository)
            .withMandatoryData(
                name = "Piso de diseño moderno en el centro de la ciudad",
                description = "Un moderno apartamento tipo estudio convenientemente ubicado en el centro de la ciudad de Odessa. A poca distancia de la calle Deribasovskaya, la estacion de tren y la playa. El departamento está situado en el primer piso y tiene una entrada separada de la calle. Recientemente renovado. Apartamento moderno, convenientemente ubicado en el centro de Odessa. Se puede caminar hasta la calle Deribasovskaya, la estacion de tren y la playa. Esta ubicado en la planta baja y tiene una entrada privada desde la calle. Recien renovado.",
                country = "Ucrania",
                address = "Av. Siempre Viva 3345",
                baseCost = 12536.55.toFloat(),
                capacity = 4,
                rooms = 1,
                baths = 1,
                detailLodging = "El espacio del estudio se divide en un dormitorio y una sala de estar separada por una interesante solucion de diseño. El espacio del estudio del apartamento se divide en un dormitorio y una sala de estar separada.",
                aspects = "Rapido wi-fi, TV digital, aire acondicionado, lavadora. Internet rapido, TV digital, aire acondicionado, lavadora.",
                owner = nicolas
            )
            .withImage("https://www.hogares.cl/wp-content/uploads/2018/06/SLA_3734.jpg")
            .withHouseKeeping(true)
            .save()

        LodgingBuilder(Building(), lodgingRepository)
            .withMandatoryData(
                name = "Apartamento en el centro histórico",
                description = "Un apartamento moderno y elegante ubicado en el centro histórico de Madrid. A poca distancia de los principales museos y atracciones turísticas de la ciudad.",
                country = "España",
                address = "Calle Mayor 123",
                baseCost = 5000.toFloat(),
                capacity = 4,
                rooms = 2,
                baths = 1,
                detailLodging = "El apartamento es moderno y elegante, con una decoración cuidada y detalles de diseño.",
                aspects = "Cerca de museos, aire acondicionado, cocina equipada",
                owner = nicolas
            )
            .withImage("https://media-cdn.tripadvisor.com/media/vr-splice-j/03/40/f7/b6.jpg")
            .withHouseKeeping(false)
            .save()

        LodgingBuilder(Building(), lodgingRepository)
            .withMandatoryData(
                name = "Apartamento en el barrio gótico",
                description = "Un apartamento luminoso y moderno ubicado en el corazón del barrio gótico de Barcelona. Rodeado de bares, restaurantes y tiendas, es el lugar perfecto para explorar la ciudad.",
                country = "España",
                address = "Plaza Reial 12",
                baseCost = 6000.toFloat(),
                capacity = 4,
                rooms = 1,
                baths = 1,
                detailLodging = "El apartamento es luminoso y moderno, con una decoración cuidada y detalles de diseño.",
                aspects = "Cerca de bares y restaurantes, balcón, aire acondicionado",
                owner = leandro
            )
            .withImage("https://cdn.pixabay.com/photo/2017/03/22/17/39/kitchen-2165756_960_720.jpg")
            .withHouseKeeping(true)
            .save()

        LodgingBuilder(Building(), lodgingRepository)
            .withMandatoryData(
                name = "Loft en el Soho",
                description = "Un loft amplio y moderno ubicado en el famoso barrio de Soho en Nueva York. Rodeado de galerías de arte y tiendas de moda, es el lugar perfecto para los amantes de la cultura y la moda.",
                country = "Estados Unidos",
                address = "Broadway 1234",
                baseCost = 8000.toFloat(),
                capacity = 4,
                rooms = 1,
                baths = 1,
                detailLodging = "El loft es amplio y moderno, con una decoración cuidada y detalles de diseño.",
                aspects = "Cerca de galerías de arte, aire acondicionado, cocina equipada",
                owner = matias
            )
            .withImage("https://st.hzcdn.com/simgs/pictures/kitchens/soho-loft-rooftop-expansion-john-muggenborg-architectural-photography-img~7061f3b70239ce2b_4-0343-1-24da5bb.jpg")
            .withHouseKeeping(true)
            .save()

        //---------------------------------- CABAÑAS ----------------------------------

        LodgingBuilder(Hut(), lodgingRepository)
            .withMandatoryData(
                name = "Cabaña de madera lujosa en el bosque",
                description = "Una acogedora cabaña de madera ubicada en un entorno natural y tranquilo. Equipada con todas las comodidades modernas, incluyendo una cocina completa, baño privado y área de estar cómoda. La cabaña cuenta con amplios ventanales que ofrecen vistas impresionantes del bosque circundante y la vida silvestre. El espacio al aire libre incluye una terraza con muebles de jardín y una barbacoa. Perfecto para una escapada de fin de semana para relajarse y disfrutar de la naturaleza.",
                country = "Argentina",
                address = "La Pampa 2645",
                baseCost = 14256.toFloat(),
                capacity = 4,
                rooms = 1,
                baths = 1,
                detailLodging = "La cabaña de madera tiene una habitación que funciona como dormitorio y sala de estar. La cocina está completamente equipada y el baño privado tiene una ducha y toallas limpias. Cuenta con una amplia terraza con vistas panorámicas del bosque, con muebles de jardín y una barbacoa. Ubicada en un entorno natural tranquilo y rodeada de vida silvestre, es el lugar perfecto para relajarse y disfrutar de la naturaleza.",
                aspects = "Wi-Fi rápido, TV digital, aire acondicionado y lavadora.",
                owner = leandro
            )
            .withImage("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/caban-a-disen-o-actual-1535369712.jpg?crop=0.728xw:0.814xh;0.116xw,0.186xh&resize=640:*")
            .withHouseKeeping(true)
            .save()

        LodgingBuilder(Hut(), lodgingRepository)
            .withMandatoryData(
                name = "Cabaña en el bosque de Patagonia",
                description = "Una cabaña rústica y acogedora ubicada en el bosque de Patagonia. Ideal para desconectar y disfrutar de la naturaleza en su estado más puro.",
                country = "Argentina",
                address = "Ruta 40 km 2000",
                baseCost = 1500.toFloat(),
                capacity = 2,
                rooms = 1,
                baths = 1,
                detailLodging = "La cabaña es rústica y acogedora, con una decoración cuidada y detalles de diseño.",
                aspects = "Cerca de la naturaleza, chimenea, aire acondicionado",
                owner = rodrigo
            )
            .withImage("https://www.bosquedormido.com.ar/web/wp-content/uploads/2016/10/lugar-nueva-4.jpg")
            .withHouseKeeping(false)
            .save()

        LodgingBuilder(Hut(), lodgingRepository)
            .withMandatoryData(
                name = "Cabaña con vista al lago",
                description = "Una cabaña con una vista espectacular al lago Nahuel Huapi, en la Patagonia argentina. Ideal para los amantes de la naturaleza y el trekking.",
                country = "Argentina",
                address = "Ruta 231 km 20",
                baseCost = 2000.toFloat(),
                capacity = 4,
                rooms = 2,
                baths = 1,
                detailLodging = "La cabaña cuenta con una vista espectacular al lago y está decorada con un estilo rústico y acogedor.",
                aspects = "Vista al lago, chimenea, cocina equipada",
                owner = alex
            )
            .withImage("https://i.pinimg.com/originals/68/6a/ff/686aff840c2a709e49fcfb82213897e0.jpg")
            .withHouseKeeping(true)
            .save()

        LodgingBuilder(Hut(), lodgingRepository)
            .withMandatoryData(
                name = "Cabaña de montaña",
                description = "Una cabaña acogedora y romántica ubicada en las montañas de Suiza. Ideal para una escapada en pareja.",
                country = "Suiza",
                address = "Rue de la Montagne 123",
                baseCost = 2500.toFloat(),
                capacity = 2,
                rooms = 1,
                baths = 1,
                detailLodging = "La cabaña es acogedora y romántica, con una decoración cuidada y detalles de diseño.",
                aspects = "Cerca de las montañas, chimenea, bañera de hidromasaje",
                owner = leandro
            )
            .withImage("https://images.unsplash.com/photo-1475087542963-13ab5e611954?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fGNhYmElQzMlQjFhJTIwZGUlMjBtb250YSVDMyVCMWF8ZW58MHx8MHx8&w=1000&q=80")
            .withHouseKeeping(true)
            .save()

        //this.neoLodgingRepository.save(lodging_1)
        //this.neoLodgingRepository.save(lodging_2)
        //this.neoLodgingRepository.save(lodging_3)

    }
//
//    @Transactional
//    private fun createBalanceTrigger() {
//        jdbcTemplate.execute("""
//            CREATE TABLE IF NOT EXISTS balance_history (
//                id SERIAL PRIMARY KEY,
//                user_id INTEGER,
//                previous_balance NUMERIC,
//                current_balance NUMERIC,
//                timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW()
//            );
//
//            CREATE OR REPLACE FUNCTION user_balance_update()
//            RETURNS TRIGGER AS ${'$'}${'$'}
//            BEGIN
//                IF NEW.balance <> OLD.balance THEN
//                    INSERT INTO balance_history(user_id, previous_balance, current_balance)
//                    VALUES (NEW.id, OLD.balance, NEW.balance);
//                END IF;
//                RETURN NEW;
//            END;
//            ${'$'}${'$'} LANGUAGE plpgsql;
//
//            CREATE TRIGGER update_user_balance
//            AFTER UPDATE ON user_info
//            FOR EACH ROW
//            EXECUTE FUNCTION user_balance_update();
//        """)
//
//        /*  ---- Modo de uso ----
//            SELECT user_id, COUNT(*) FROM balance_history
//            GROUP BY user_id
//            ORDER BY user_id;
//        * */
//    }
//
//    @Transactional
//    private fun priceNullConstraint() {
//        jdbcTemplate.execute("""
//            ALTER TABLE lodging
//            ADD CONSTRAINT non_null_base_cost
//            CHECK (base_cost IS NOT NULL);
//        """)
//    }
//
//
//    @Transactional
//    private fun userBookingsView() {
//        jdbcTemplate.execute("""
//            CREATE OR REPLACE VIEW user_bookings AS
//            SELECT b.user_id as user_id, l.id, l.name, l.dtype FROM booking as b
//            INNER JOIN lodging as l on b.lodging_id = l.id;
//        """)
//
//        /*  ---- Modo de uso ----
//            SELECT * FROM user_bookings
//            WHERE user_id = 1;
//        * */
//    }
//
//    @Transactional
//    private fun usersWithMoreThan3BookingView() {
//        jdbcTemplate.execute("""
//            CREATE OR REPLACE VIEW user_with_more3_booking AS
//            SELECT u.id, u.user_name, COUNT(*) AS bookings_count
//            FROM user_info as u
//            INNER JOIN booking as b ON u.id = b.user_id
//            GROUP BY u.id
//            HAVING COUNT(*) > 3;
//        """)
//
//        /*  ---- Modo de uso ----
//            SELECT * FROM user_with_more3_booking;
//        * */
//    }
//
//    @Transactional
//    private fun createAverageScoreUpdater(){
//        jdbcTemplate.execute("""
//            CREATE OR REPLACE FUNCTION update_lodging_average_score()
//            RETURNS TRIGGER AS ${'$'}${'$'}
//            DECLARE
//                new_average_score FLOAT;
//                target_lodging_id BIGINT;
//            BEGIN
//                -- Obtener el lodging_id de la tabla "booking"
//                SELECT b.lodging_id INTO target_lodging_id
//                FROM booking b
//                WHERE b.id = NEW.booking_id;
//
//                -- Calcular el nuevo promedio de puntaje
//                SELECT AVG(r.score) INTO new_average_score
//                FROM review r
//                JOIN booking b ON r.booking_id = b.id
//                WHERE b.lodging_id = target_lodging_id;
//
//                -- Actualizar el promedio de puntaje en la tabla "lodging"
//                UPDATE lodging
//                SET average_score = new_average_score
//                WHERE id = target_lodging_id;
//
//                RETURN NEW;
//            END;
//            ${'$'}${'$'} LANGUAGE plpgsql;
//
//            CREATE TRIGGER update_lodging_average_score_trigger
//            AFTER INSERT OR UPDATE OR DELETE
//            ON review
//            FOR EACH ROW
//            EXECUTE FUNCTION update_lodging_average_score();
//        """)
//    }
//
//    private fun createDataBaseObjects(){
//        // --------- Parte 2 -------- //
//        this.createBalanceTrigger()
//        this.priceNullConstraint()
//        this.userBookingsView()
//        this.usersWithMoreThan3BookingView()
//        // No tiene ningun sentido utilizar un procedure en ninguna de las opciones de la consigna
//
//        // --------- Objetos necesarios por el programa principal -------- //
//        this.createAverageScoreUpdater()
//    }
//
//    private fun cleanup() {
//        jdbcTemplate.execute("""
//            DROP TABLE IF EXISTS balance_history;
//
//            DROP TRIGGER IF EXISTS update_lodging_average_score_trigger ON review;
//            DROP FUNCTION IF EXISTS update_lodging_average_score();
//            DROP TRIGGER IF EXISTS update_user_balance ON user_info;
//            DROP FUNCTION IF EXISTS user_balance_update();
//
//            DROP VIEW IF EXISTS user_bookings;
//            DROP VIEW IF EXISTS user_with_more3_booking;
//        """)
//    }
//


//    fun createMongoLodging(){ //Funcion para probar, despues se borra
//        this.lodgingRepository.save(
//            Building(
//                name = "Casa de Nicolas",
//                description = "Casa de Nicolas en Dubai",
//                address = "Calle falsa 123",
//                detailLodging = "Casa de 2 pisos con pileta",
//                capacity = 5,
//                baseCost = 1000.0.toFloat(),
//                country = "Argentina",
//                houseKeeping = true,
//                aspects = "pileta, 2 pisos, 3 habitaciones, 2 baños",
//                numOfRooms = 3,
//                user = nicolas
//            )
//        )
//    }
    override fun afterPropertiesSet() {
        this.saveUsers()
//        this.createMongoLodging()
//        this.createDataBaseObjects()
        this.saveLodgings()
    }

    fun cleanup() {
        this.lodgingRepository.deleteAll()
        this.neoUserRepository.deleteAll()
        this.neoLodgingRepository.deleteAll()
    }

    override fun destroy() {
        if(dbConfig == "create-drop")
            cleanup()
    }
}