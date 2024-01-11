/*
 * Copyright (C) 2018 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.velocitypowered.proxy.config;

/**
 * This enum represents the different policies for key authentication in the Velocity proxy.
 * This is mainly used to be able to cancel chat messages on proxy, opting-out of encryption.
 * The policies include:
 * <ul>
 *   <li>DISABLED: Key authentication is not checked, all chat messages will appear unsigned.</li>
 *   <li>ENABLED: The proxy will support secure profiles but also allow insecure profiles.</li>
 *   <li>ENFORCED: The proxy will only allow secure profiles to log in.</li>
 * </ul>
 */
public enum KeyAuthenticationPolicy {
    DISABLED,
    ENABLED,
    ENFORCED
}