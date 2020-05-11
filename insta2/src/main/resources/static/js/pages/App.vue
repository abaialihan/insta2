<template>
    <v-app id="inspire">
        <v-app-bar app clipped left>
            <v-toolbar-title>Insta2.0</v-toolbar-title>
            <v-spacer></v-spacer>
            <span v-if="profile">{{profile.name}}</span>
            <v-btn v-if="profile" icon href="/logout">
                <v-icon>mdi-location-exit</v-icon>
            </v-btn>
        </v-app-bar>
        <v-content>
            <v-container v-if="!profile">You need authorization from
                <a href="/login">Google</a>
            </v-container>
            <v-container v-if="profile">
                <messages-list :messages="messages" />
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import MessagesList from 'components/messages/MessageList.vue'
    import { addHandler } from 'util/ws'

    export default {
        components: {
            MessagesList
        },
        created() {
            addHandler(data => {
                if (data.objectType === 'MESSAGE'){
                    let index = this.messages.findIndex(item => item.id === data.body.id)
                    switch(data.eventType){
                        case 'CREATE':
                        case 'UPDATE':
                            if (index > 1){
                                this.messages.splice(index, 1, data.body)
                            }else{
                                this.messages.push(data.body)
                            }
                            break
                        case 'REMOVE':
                            this.messages.splice(index, 1)
                            break
                        default:
                            console.error('event type is unknown "${data.eventType}"')
                    }
                }else{
                    console.error('object type is unknown"${data.objectType}"')
                }
            })
        }
    }
</script>

<style>
    .main-app{
        color: maroon;
    }
</style>