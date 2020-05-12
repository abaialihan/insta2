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
                <messages-list />
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import { mapState, mapMutations } from 'vuex'
    import MessagesList from 'components/messages/MessageList.vue'
    import { addHandler } from 'util/ws'
    export default {
        components: {
            MessagesList
        },
        computed: mapState(['profile']),
        methods: mapMutations(['addMessageMutation', 'updateMessageMutation', 'removeMessageMutation']),
        created() {
            addHandler(data => {
                if (data.objectType === 'MESSAGE') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addMessageMutation(data.body)
                            break
                        case 'UPDATE':
                            this.updateMessageMutation(data.body)
                            break
                        case 'REMOVE':
                            this.removeMessageMutation(data.body)
                            break
                        default:
                            console.error(`event type if unknown "${data.eventType}"`)
                    }
                } else {
                    console.error(`object type if unknown "${data.objectType}"`)
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