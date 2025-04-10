PAPI expansion that allows you to fetch a plugin's permission from its plugin.yml configuration, you can fetch this data:

Name of the permission, default state, description.

The format is:

`%permissionfetch_[action]_[pluginName]_[position]%`

Where: <br>
--> `[action]` : can be `fetch`, `default` and `description` <br>
--> `[pluginName]` : is also specified in the `plugin.yml` <br>
--> `[position]` : using the `permissions` node in the `plugin.yml`, the position of the permission you want <br>

Here are some examples:

Example file used: https://github.com/Intybyte/Cannons/blob/master/cannons-bukkit/src/main/resources/plugin.yml

`%permissionfetch_fetch_Cannons_0%` --> `cannons.*` <br>
`%permissionfetch_default_Cannons_0%` --> `false` <br>
`%permissionfetch_description_Cannons_0%` --> `Gives  permissions for loading, adjusting, firing and recall help with cannons` <br>

`%permissionfetch_fetch_Cannons_1%`  --> `cannons.player.*` <br>
`%permissionfetch_default_Cannons_1%` --> `op` <br>
`%permissionfetch_description_Cannons_1%` --> `Gives single permissions for loading, adjusting, firing and recall help with cannons` <br>
