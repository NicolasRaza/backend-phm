import {createClient} from 'redis'

(async () => {

    console.log('CARGANDO DATA ==================================')
    const client = createClient({
        socket: {
            host: 'redis-airphm', port: '6379'
        }
    })
    // console.info('ejecutando scripts')
    //
    client.on('error', (err) => {
        console.error('Error ' + err)
    })

    await client.connect()
    console.info('connected')

    await client.del('dolar') // elimina la clave si existe
    await client.lPop('dolar')
    await client.rPush('dolar', '62.36')
    await client.rPush('dolar', '65.43')
    await client.set('real', '13.6')
    await client.set('euro', '69.48')

    await client.quit()
    console.info('valores actualizados.')
})()
