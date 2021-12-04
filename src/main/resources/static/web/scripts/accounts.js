const app = Vue.createApp({

    data() {
        return {
            clientAccounts: [],
            accounts: [],
            clientLoans: [],
        }
    },

    created() {
        axios.get('http://localhost:8080/api/clients/current',{headers:{'accept':'application/xml'}})
        
        .then(response => response.data)
        
        axios.get('/api/clients/current')
        .then(response => {
            this.clientAccounts = response.data
            this.accounts = response.data.accounts.filter( account=> account.accountDelete == false)
            this.clientLoans = response.data.clientLoans
            this.ordenarIdAcoounst(this.accounts)
            this.crearUrl() 
            this.horaActual()
        })
        .catch(error => {
            return error.message;
        })

    },
    methods: {
              
        creandoUnaUrl(id) {
            return `account.html?id=${id}`
        },

        ordenarIdAcoounst(arrayClientAccounts){
            
            let ordenandoId = arrayClientAccounts.sort((idA, idB) => {
                if (idA.id > idB.id) { return  1}
                if (idA.id < idB.id) { return -1}
                return 0
            })
            return ordenandoId
        },

        eliminarCuenta(number){

                Swal.fire({

                    title: 'Are you sure?',
                    text: "You won't be able to revert this!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes!'

              }).then((result) => {

                if (result.isConfirmed) {

                  Swal.fire(
                    'Successful!',
                    'Successfully related.',
                    'success'
                  )
                  axios.delete(`/clients/delete/account/${number}`, {headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        console.log('OK');
                        location.reload()
                    })
                    .catch(error => {

                        Swal.fire({
                            icon: 'error',
                            title: `Oops...${error.response.data}`,
                          })
                    })

                } else {
                    setTimeout(location.reload.bind(location), 500)
                }
            })
        },

        cerrar() {
            axios.post('/api/logout')

            .then(response => { 
                window.location.replace("/web/index.html")
             })
             .catch(error => {
                error.message
            })
                
        },

        enConstruccion() {

            Swal.fire({

                icon: 'info',
                title: 'Oops...',
                text: 'We are working on it, Thank you for your understanding!',
                
              })
        },
        
    }, //llave methods

})

const verAppVue = app.mount("#app") 
var swiper = new Swiper(".mySwiper", {
    effect: "cube",
    grabCursor: true,
    cubeEffect: {
      shadow: true,
      slideShadows: true,
      shadowOffset: 20,
      shadowScale: 0.94,
    },
    autoplay: {
      delay: 3000
    },
    loop: true,
    pagination: {
      el: ".swiper-pagination",
    },
  });

